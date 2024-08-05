package com.icell.external.carlosformito.core

import android.util.Log
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.core.api.validator.CrossFormFieldValidator
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.core.api.validator.IsFormFieldValidator
import com.icell.external.carlosformito.core.validator.ValueRequiredValidator
import com.icell.external.carlosformito.core.validator.connections.ConnectionValidator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * Manages a collection of form fields, providing validation, state management, and visibility control.
 *
 * @property formFields The list of form fields to manage.
 * @property validationStrategy The strategy for field validation, defaults to [FormFieldValidationStrategy.MANUAL].
 */
@Suppress("TooManyFunctions")
class CarlosFormManager(
    private val formFields: List<FormField<*>>,
    private val validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.MANUAL
) : FormManager {

    /**
     * Map for quick accessing the flow of [FormFieldState]s by field ID
     */
    private val fieldStates: HashMap<String, MutableStateFlow<FormFieldState<*>>> = hashMapOf()

    /**
     * Map for quick accessing the [FormFieldItem]s by field ID
     */
    private val fieldItems: HashMap<String, FormFieldItem<*>> = hashMapOf()

    /**
     * Map for quick accessing connected field IDs by field ID
     */
    private val fieldConnections: HashMap<String, Set<String>> = hashMapOf()

    /**
     * A state flow for tracking the visibility of each form field
     */
    private val fieldVisibility: MutableStateFlow<Map<String, Boolean>> = MutableStateFlow(emptyMap())

    /**
     * List of IDs for required fields
     */
    private val requiredFieldIds: MutableList<String> = mutableListOf()

    // State flow for tracking whether all required fields are filled
    private val mutableAllRequiredFieldFilled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val allRequiredFieldFilled = mutableAllRequiredFieldFilled.asStateFlow()

    // State flow indicating whether validation is in progress
    private val mutableValidationInProgress = MutableStateFlow(false)
    override val validationInProgress = mutableValidationInProgress.asStateFlow()

    // Flag to track initialization state
    private var initialized: Boolean = false

    // Coroutine variables for auto-validation
    private var autoValidationJob: Job? = null
    private lateinit var autoValidationScope: CoroutineScope
    private lateinit var autoValidationContext: CoroutineContext

    init {
        formFields.forEach { field ->
            fieldStates[field.id] = MutableStateFlow(field.initialState)
            fieldItems[field.id] = createFieldItem(field)

            field.validators.forEach { validator ->
                when (validator) {
                    is ValueRequiredValidator -> requiredFieldIds.add(field.id)
                    is ConnectionValidator<*> -> {
                        val connections = fieldConnections[validator.connectedFieldId]?.toMutableSet() ?: mutableSetOf()
                        fieldConnections[validator.connectedFieldId] = connections.apply { add(field.id) }
                    }
                    else -> Unit
                }
            }
        }
    }

    /**
     * Initializes the form manager, setting up auto-validation and field visibility checks.
     *
     * @param autoValidationExceptionHandler Optional exception handler for auto-validation.
     */
    @OptIn(FlowPreview::class)
    override suspend fun initFormManager(autoValidationExceptionHandler: CoroutineExceptionHandler?) {
        coroutineScope {
            initialized = true
            autoValidationScope = this
            autoValidationContext = autoValidationExceptionHandler ?: EmptyCoroutineContext

            fieldVisibility.debounce(FIELD_VISIBILITY_UPDATE_DEBOUNCE).collectLatest { _ ->
                checkAllRequiredFieldFilled()
            }
        }
    }

    /**
     * Launches auto-validation with the provided validation block on the specified auto-validation scope and context.
     *
     * @param validationBlock The suspend function to perform validation.
     */
    private fun launchAutoValidation(validationBlock: suspend () -> Unit) {
        autoValidationJob?.cancel()
        autoValidationJob = autoValidationScope.launch(autoValidationContext) {
            monitorValidationProgress(validationBlock)
        }
    }

    /**
     * Monitors the progress of a validation block, updating the [validationInProgress] state accordingly.
     *
     * @param validationBlock The block of code representing the validation process.
     */
    private suspend fun monitorValidationProgress(validationBlock: suspend () -> Unit) {
        try {
            mutableValidationInProgress.value = true
            validationBlock()
            mutableValidationInProgress.value = false
        } catch (throwable: Throwable) {
            mutableValidationInProgress.value = false
            throw throwable
        }
    }

    /**
     * Creates a [FormFieldItem] for a given [FormField].
     *
     * @param formField The form field for which to create the item.
     * @return The created form field item.
     */
    private fun <T> createFieldItem(formField: FormField<T>): FormFieldItem<T> {
        val fieldItem = CarlosFormFieldItem(
            fieldId = formField.id,
            fieldState = getFieldStateFlow<T>(formField.id)
        )
        return fieldItem.also { fieldItem.setListener(this) }
    }

    /**
     * Retrieves the state flow of [FormFieldState] for a specific form field ID.
     *
     * @param id The ID of the form field.
     * @return The state flow of [FormFieldState].
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> getFieldStateFlow(id: String): MutableStateFlow<FormFieldState<T>> {
        return requireNotNull(fieldStates[id] as MutableStateFlow<FormFieldState<T>>)
    }

    /**
     * Retrieves the [FormFieldItem] for the specified field ID.
     *
     * @param id The ID of the form field.
     * @return The [FormFieldItem] associated with the ID.
     * @throws IllegalStateException if the form manager has not been initialized.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T> getFieldItem(id: String): FormFieldItem<T> {
        return requireNotNull(fieldItems[id] as FormFieldItem<T>)
    }

    /**
     * Handles the event when the focus is cleared from a field.
     *
     * @param id The ID of the field.
     */
    override fun onFieldFocusCleared(id: String) {
        checkInitialized()
        if (validationStrategy == FormFieldValidationStrategy.AUTO_ON_FOCUS_CLEAR) {
            launchAutoValidation {
                validateAndUpdateFieldState(id)
                validateFieldConnections(id)
            }
        }
    }

    /**
     * Handles the event when the value of a field changes.
     *
     * Clears the validation result, updates the required fields filling state,
     * and triggers auto-validation if the validation strategy is set to [FormFieldValidationStrategy.AUTO_INLINE].
     *
     * @param id The ID of the field.
     * @param value The new value of the field.
     */
    override fun <T> onFieldValueChanged(id: String, value: T?) {
        checkInitialized()
        getFieldStateFlow<T>(id).update { state ->
            state.copy(value, validationResult = null)
        }
        if (requiredFieldIds.contains(id)) {
            checkAllRequiredFieldFilled()
        }
        when (validationStrategy) {
            FormFieldValidationStrategy.MANUAL -> {
                fieldConnections[id]?.forEach { connectedFieldId ->
                    getFieldStateFlow<T>(connectedFieldId).update { state ->
                        state.copy(validationResult = null)
                    }
                }
            }

            FormFieldValidationStrategy.AUTO_INLINE -> {
                launchAutoValidation {
                    validateAndUpdateFieldState(id)
                    validateFieldConnections(id)
                }
            }

            else -> {
                // no - op
            }
        }
    }

    /**
     * Handles the event when the visibility of a field changes.
     *
     * @param id The ID of the field.
     * @param visible The new visibility state of the field.
     */
    override fun onFieldVisibilityChanged(id: String, visible: Boolean) {
        checkInitialized()
        if (fieldVisibility.value[id] != visible) {
            fieldVisibility.update { visibilityMap ->
                visibilityMap.toMutableMap().apply { put(id, visible) }
            }
        }
    }

    /**
     * Checks if all required fields that are currently visible are filled.
     */
    private fun checkAllRequiredFieldFilled() {
        mutableAllRequiredFieldFilled.value = requiredFieldIds
            .filter { id -> fieldVisibility.value[id] == true }
            .all { id -> checkFieldFilled(id) }
    }

    /**
     * Checks if the form field with the specified ID is filled.
     *
     * This function retrieves the state of the form field identified by the given ID
     * and determines if it is filled.
     *
     * @param id The identifier of the form field to check.
     * @return `true` if the form field is filled, `false` otherwise.
     */
    private fun checkFieldFilled(id: String): Boolean = fieldStates.getValue(id).value.isFilled

    /**
     * Retrieves the current value of the form field associated with the given ID.
     *
     * @param id The unique identifier of the form field.
     * @return The current value of the form field, or `null` if the field is not found or its value is `null`.
     * @throws NoSuchElementException If the field with the specified ID does not exist.
     */
    override fun <T> getFieldValue(id: String): T? = getFieldItem<T>(id).fieldState.value.value

    /**
     * Validates the entire form, including individual field validations and connections between fields.
     *
     * @return `true` if the form is valid, otherwise `false`.
     */
    override suspend fun validateForm(): Boolean {
        checkInitialized()
        var isFormValid = false

        monitorValidationProgress {
            isFormValid = validateIndividualFields()
        }

        if (BuildConfig.DEBUG) {
            printFormState()
        }

        return isFormValid
    }

    /**
     * Validates all individual fields in the form.
     *
     * @return `true` if all individual fields are valid, otherwise `false`.
     */
    private suspend fun validateIndividualFields(): Boolean {
        return fieldStates.keys
            .map { id -> validateAndUpdateFieldState(id) }
            .all { isValid -> isValid }
    }

    /**
     * Validates and updates the state of a specific field.
     *
     * @param id The ID of the field to validate.
     * @return `true` if the field is valid, otherwise `false`.
     */
    private suspend fun validateAndUpdateFieldState(id: String): Boolean {
        /**
         * Skip validation for invisible fields
         */
        if (fieldVisibility.value[id] != true) {
            return true
        }
        val fieldState = fieldStates.getValue(id)
        val validationResult = validateField(id, fieldState.value.value)
        fieldState.update { state -> state.copy(validationResult = validationResult) }
        return validationResult is FormFieldValidationResult.Valid
    }

    /**
     * Validates a specific field based on its ID and current value.
     *
     * @param id The ID of the field to validate.
     * @param fieldValue The current value of the field.
     * @return The validation result for the field.
     */
    private suspend fun <T> validateField(id: String, fieldValue: T?): FormFieldValidationResult {
        getValidators<T>(id).forEach { validator ->
            val result = when (validator) {
                is FormFieldValidator -> validator.validate(fieldValue)
                is CrossFormFieldValidator -> validator.validate(fieldValue, context = this)
            }
            if (result is FormFieldValidationResult.Invalid) {
                return result
            }
        }
        return FormFieldValidationResult.Valid
    }

    /**
     * Retrieves the validators associated with a field ID.
     *
     * @param id The ID of the field.
     * @return The list of validators for the field.
     */
    @Suppress("UNCHECKED_CAST")
    private fun <T> getValidators(id: String): List<IsFormFieldValidator<T>> {
        return formFields.first { item -> item.id == id }.validators as List<IsFormFieldValidator<T>>
    }

    /**
     * Validates a fields connections by field ID.
     *
     * Note: a connected field is validated only if it is filled.
     */
    private suspend fun validateFieldConnections(id: String) {
        fieldConnections[id]?.forEach { connectedFieldId ->
            if (checkFieldFilled(connectedFieldId)) {
                validateAndUpdateFieldState(connectedFieldId)
            }
        }
    }

    /**
     * Sets the form state to invalid, indicating validation failure for all fields.
     */
    override fun setFormInvalid() {
        checkInitialized()
        fieldStates.forEach { (_, fieldItemState) ->
            fieldItemState.update { state ->
                state.copy(
                    validationResult = FormFieldValidationResult.Invalid.Unknown
                )
            }
        }
    }

    /**
     * Clears the form, resetting all fields to their initial states.
     */
    override fun clearForm() {
        checkInitialized()
        formFields.forEach { formField ->
            fieldStates[formField.id]?.value = formField.initialState
        }
        checkAllRequiredFieldFilled()
    }

    /**
     * Prints the current state of the form to the debug log.
     */
    @Suppress("MagicNumber")
    override fun printFormState() {
        checkInitialized()
        val logText = buildString {
            appendLine("------Form state------")

            formFields.forEach { field ->
                val fieldState = fieldStates.getValue(field.id).value
                val row = listOf(
                    field.id.take(30).padEnd(30),
                    fieldState.value.toString().take(30).padEnd(30),
                    fieldVisibility.value[field.id].toString().padEnd(5),
                    fieldState.validationResult.toString()
                ).joinToString(" | ")
                appendLine(row)
            }

            if (formFields.isEmpty()) {
                appendLine("Empty")
            }

            append("----------------------")
        }
        Log.d("CarlosFormManager", logText)
    }

    /**
     * Checks if the form manager has been initialized before using any of its functionalities.
     */
    private fun checkInitialized() {
        if (!initialized) {
            error("Please call ${this::initFormManager.name}() before using ${CarlosFormManager::class.simpleName}!")
        }
    }

    companion object {
        private val FIELD_VISIBILITY_UPDATE_DEBOUNCE = 300.milliseconds
    }
}
