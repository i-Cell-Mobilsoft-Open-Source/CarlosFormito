package com.icell.external.carlosformito.core

import android.util.Log
import androidx.annotation.CallSuper
import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormField
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.core.validator.ValueRequiredValidator
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

@Suppress("TooManyFunctions")
open class CarlosFormManager(
    private val formFields: List<FormField<*>>,
    private val validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.MANUAL
) : FormManager {

    private val fieldStates: Map<String, MutableStateFlow<FormFieldState<*>>> =
        buildMap {
            formFields.forEach { formField ->
                put(formField.id, MutableStateFlow(formField.initialState))
            }
        }

    private val fieldItems: Map<String, FormFieldItem<*>> =
        buildMap {
            formFields.forEach { formField ->
                put(formField.id, createFieldItem(formField))
            }
        }

    private val requiredFieldIds: List<String> = formFields
        .filter { formField ->
            formField.validators.any { validator -> validator is ValueRequiredValidator }
        }
        .map { formField -> formField.id }

    private val fieldVisibility = MutableStateFlow(
        buildMap {
            formFields.forEach { formField -> put(formField.id, false) }
        }
    )

    private val mutableAllRequiredFieldFilled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val allRequiredFieldFilled = mutableAllRequiredFieldFilled.asStateFlow()

    private val mutableValidationInProgress = MutableStateFlow(false)
    override val validationInProgress = mutableValidationInProgress.asStateFlow()

    private var initialized: Boolean = false

    private var autoValidationJob: Job? = null
    private lateinit var autoValidationScope: CoroutineScope
    private lateinit var autoValidationContext: CoroutineContext

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

    private fun launchAutoValidation(validationBlock: suspend () -> Unit) {
        autoValidationJob?.cancel()
        autoValidationJob = autoValidationScope.launch(autoValidationContext) {
            monitorValidationProgress(validationBlock)
        }
    }

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

    private fun <T> createFieldItem(formField: FormField<T>): FormFieldItem<T> {
        val fieldItem = CarlosFormFieldItem(
            fieldId = formField.id,
            fieldState = getFieldStateFlow<T>(formField.id)
        )
        return fieldItem.also { fieldItem.setListener(this) }
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> getFieldStateFlow(id: String): MutableStateFlow<FormFieldState<T>> {
        return requireNotNull(fieldStates[id] as MutableStateFlow<FormFieldState<T>>)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getFieldItem(id: String): FormFieldItem<T> {
        checkInitialized()
        return requireNotNull(fieldItems[id] as FormFieldItem<T>)
    }

    override fun onFieldFocusCleared(id: String) {
        checkInitialized()
        if (validationStrategy == FormFieldValidationStrategy.AUTO_ON_FOCUS_CLEAR) {
            launchAutoValidation {
                validateAndUpdateFieldState(id)
            }
        }
    }

    override fun <T> onFieldValueChanged(id: String, value: T?) {
        checkInitialized()
        val currentFieldState = getFieldStateFlow<T>(id)
        currentFieldState.update { state ->
            state.copy(
                value = value,
                validationResult = null // Clear validation result on value change
            )
        }
        if (requiredFieldIds.contains(id)) {
            checkAllRequiredFieldFilled()
        }
        if (validationStrategy == FormFieldValidationStrategy.AUTO_INLINE) {
            launchAutoValidation {
                validateAndUpdateFieldState(id)
            }
        }
    }

    override fun onFieldVisibilityChanged(id: String, visible: Boolean) {
        checkInitialized()
        if (fieldVisibility.value[id] != visible) {
            fieldVisibility.update { visibilityMap ->
                visibilityMap.toMutableMap().apply { put(id, visible) }
            }
        }
    }

    private fun checkAllRequiredFieldFilled() {
        mutableAllRequiredFieldFilled.value = requiredFieldIds
            .filter { id -> fieldVisibility.value[id] == true }
            .map { id -> fieldStates[id]?.value?.value }
            .all { fieldValue ->
                if (fieldValue is String) {
                    fieldValue.isNotBlank()
                } else {
                    fieldValue != null
                }
            }
    }

    override suspend fun validateForm(): Boolean {
        checkInitialized()
        var isFormValid = false

        monitorValidationProgress {
            isFormValid = validateIndividualFields() && validateFieldConnections()
        }

        if (BuildConfig.DEBUG) {
            printFormState()
        }

        return isFormValid
    }

    private suspend fun validateIndividualFields(): Boolean {
        return fieldStates.keys
            .map { id -> validateAndUpdateFieldState(id) }
            .all { isValid -> isValid }
    }

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

    @CallSuper
    protected suspend fun <T> validateField(id: String, fieldValue: T?): FormFieldValidationResult {
        getValidators<T>(id).forEach { validator ->
            val result = validator.validate(fieldValue)
            if (result is FormFieldValidationResult.Invalid) {
                return result
            }
        }
        return FormFieldValidationResult.Valid
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getValidators(id: String): List<FormFieldValidator<T>> {
        return formFields.first { item -> item.id == id }.validators as List<FormFieldValidator<T>>
    }

    protected open suspend fun validateFieldConnections(): Boolean {
        return true
    }

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

    override fun clearForm() {
        checkInitialized()
        formFields.forEach { formField ->
            fieldStates[formField.id]?.value = formField.initialState
        }
        checkAllRequiredFieldFilled()
    }

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

    private fun checkInitialized() {
        if (!initialized) {
            error("Please call ${this::initFormManager.name}() before using ${CarlosFormManager::class.simpleName}!")
        }
    }

    companion object {
        private val FIELD_VISIBILITY_UPDATE_DEBOUNCE = 300.milliseconds
    }
}
