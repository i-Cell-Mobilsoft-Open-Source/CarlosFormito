package hu.icellmobilsoft.carlosformito.core

import android.util.Log
import hu.icellmobilsoft.carlosformito.core.api.FormFieldItem
import hu.icellmobilsoft.carlosformito.core.api.FormManager
import hu.icellmobilsoft.carlosformito.core.api.model.FormField
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldState
import hu.icellmobilsoft.carlosformito.core.api.model.FormFieldValidationStrategy
import hu.icellmobilsoft.carlosformito.core.api.validator.CrossFormFieldValidator
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import hu.icellmobilsoft.carlosformito.core.api.validator.IsFormFieldValidator
import hu.icellmobilsoft.carlosformito.core.validator.ValueRequiredValidator
import hu.icellmobilsoft.carlosformito.core.validator.connections.ConnectionValidator
import hu.icellmobilsoft.carlosformito.core.validator.connections.MultiConnectionValidator
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.collections.forEach
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.coroutineContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Manages a collection of form fields, providing validation, state management, and visibility control.
 *
 * @property formFields The list of form fields to manage.
 * @property validationStrategy The strategy for field validation, defaults to [FormFieldValidationStrategy.Manual].
 */
@Suppress("TooManyFunctions")
class CarlosFormManager(
    private val formFields: List<FormField<*>>,
    private val validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.Manual
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
     * Map for quick accessing the fields custom validation strategies
     */
    private val fieldCustomValidationStrategies: HashMap<String, FormFieldValidationStrategy> = hashMapOf()

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

            field.customValidationStrategy?.let { strategy ->
                fieldCustomValidationStrategies[field.id] = strategy
            }

            field.validators.forEach { validator ->
                when (validator) {
                    is ValueRequiredValidator -> {
                        requiredFieldIds.add(field.id)
                    }

                    is ConnectionValidator<*> -> {
                        setFieldConnection(field, validator.connectedFieldId)
                    }

                    is MultiConnectionValidator<*> -> {
                        validator.connectedFieldIds.forEach { connectedFieldId ->
                            setFieldConnection(field, connectedFieldId)
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    /**
     * Registers a dependency between two form fields for automatic cross-field validation.
     *
     * @param field The field whose validity depends on the value of [connectedFieldId].
     * @param connectedFieldId The ID of the field that [field] is connected to.
     */
    private fun setFieldConnection(field: FormField<*>, connectedFieldId: String) {
        val connections = fieldConnections[connectedFieldId]?.toMutableSet() ?: mutableSetOf()
        fieldConnections[connectedFieldId] = connections.apply { add(field.id) }
    }

    /**
     * Initializes the form manager, setting up auto-validation and field visibility checks.
     *
     * @param autoValidationExceptionHandler Optional exception handler for auto-validation.
     */
    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    override suspend fun initFormManager(autoValidationExceptionHandler: CoroutineExceptionHandler?) {
        // Return if already initialized
        if (initialized) return

        autoValidationContext = autoValidationExceptionHandler ?: EmptyCoroutineContext

        val detachedJob = Job()

        // Tie the detached job lifecycle to the callers job
        coroutineContext[Job]?.parent?.invokeOnCompletion {
            detachedJob.cancel()
        }

        // Create scope that preserves caller's dispatcher but uses our detached job
        autoValidationScope = CoroutineScope(coroutineContext + detachedJob)
        autoValidationScope.launch(autoValidationContext) {
            fieldVisibility
                .debounce(FIELD_VISIBILITY_UPDATE_DEBOUNCE)
                .collectLatest {
                    checkAllRequiredFieldFilled()
                }
        }

        initialized = true
    }

    /**
     * Launches auto-validation with the provided validation block on the specified auto-validation scope and context.
     *
     * @param validationDelay An optional delay parameter for delaying the fields validation
     * @param validationBlock The suspend function to perform validation.
     */
    private fun launchAutoValidation(validationDelay: Duration? = null, validationBlock: suspend () -> Unit) {
        autoValidationJob?.cancel()
        autoValidationJob = autoValidationScope.launch(autoValidationContext) {
            validationDelay?.let { delay(validationDelay) }
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
        val strategy = fieldCustomValidationStrategies[id] ?: validationStrategy
        if (strategy == FormFieldValidationStrategy.AutoOnFocusClear) {
            launchAutoValidation {
                validateAndUpdateFieldState(id)
                validateFieldConnections(id)
            }
        }
    }

    /**
     * Handles changes to the value of a form field.
     *
     * This method updates the field's state when its value changes, ensuring that
     * the form remains consistent and that validation logic is applied according
     * to the configured validation strategy.
     *
     * Behavior:
     * - Updates the field's state with the new [value].
     * - Clears any existing [FormFieldValidationResult] and resets validation progress.
     * - Triggers auto-validation if the strategy is [FormFieldValidationStrategy.AutoInline].
     * - Automatically triggers auto-validation for the field connections if has any.
     *
     * @param id The unique identifier of the field whose value has changed.
     * @param value The new value assigned to the field, or `null` if cleared.
     *
     * @see FormFieldValidationStrategy For available validation strategies.
     * @see validateAndUpdateFieldState For how individual field validation is applied.
     * @see validateFieldConnections For how connected fields are validated after updates.
     */
    override fun <T> onFieldValueChanged(id: String, value: T?) {
        checkInitialized()
        getFieldStateFlow<T>(id).update { state ->
            state.copy(value, validationInProgress = false, validationResult = null)
        }
        if (requiredFieldIds.contains(id)) {
            checkAllRequiredFieldFilled()
        }

        val strategy = fieldCustomValidationStrategies[id] ?: validationStrategy
        val hasConnections = !fieldConnections[id].isNullOrEmpty()
        val shouldValidate = strategy is FormFieldValidationStrategy.AutoInline || hasConnections

        if (shouldValidate) {
            launchAutoValidation {
                if (strategy is FormFieldValidationStrategy.AutoInline) {
                    validateAndUpdateFieldState(id)
                }
                validateFieldConnections(id)
            }
        }
    }

    /**
     * Handles the event when a field value is reset.
     *
     * Restores the field to its initialValue and delegates the update to [onFieldValueChanged],
     * ensuring that validation state, required field tracking, and auto-validation are handled
     * consistently with normal value changes.
     *
     * @param id The ID of the field to reset.
     */
    override fun onFieldValueReset(id: String) {
        val initialFieldValue = formFields.first { item -> item.id == id }.initialValue
        onFieldValueChanged(id, initialFieldValue)
    }

    /**
     * Handles the event when the visibility of a field changes.
     *
     * @param id The ID of the field.
     * @param visible The new visibility state of the field.
     */
    override fun onFieldVisibilityChanged(id: String, visible: Boolean) {
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
     * Validates the entire form.
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
     * Validates the form field item.
     *
     * @param id The unique identifier of the form field item.
     * @return `true` if the field is valid after validation, `false` otherwise.
     */
    override suspend fun validateField(id: String): Boolean {
        checkInitialized()
        var isFormValid = false

        monitorValidationProgress {
            isFormValid = validateAndUpdateFieldState(id)
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
        fieldState.update { state -> state.copy(validationInProgress = true) }
        val validationResult = validateField(id, fieldState.value.value)
        fieldState.update { state -> state.copy(validationInProgress = false, validationResult = validationResult) }
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
                val connectedFieldStrategy = fieldCustomValidationStrategies[connectedFieldId] ?: validationStrategy
                when (connectedFieldStrategy) {
                    FormFieldValidationStrategy.Manual -> {
                        // clear validation result until explicitly validated
                        fieldStates[connectedFieldId]?.update { state ->
                            state.copy(validationInProgress = false, validationResult = null)
                        }
                    }

                    is FormFieldValidationStrategy.AutoOnFocusClear,
                    is FormFieldValidationStrategy.AutoInline -> {
                        validateAndUpdateFieldState(connectedFieldId)
                    }
                }
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
                    validationInProgress = false,
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
