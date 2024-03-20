package com.icell.external.carlosformito.core

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
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("TooManyFunctions")
open class FormManagerImpl(
    private val formFields: List<FormField<*>>,
    private val validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.MANUAL,
    override var autoValidationExceptionHandler: CoroutineExceptionHandler? = null,
    override var autoValidationScope: CoroutineScope? = null
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

    private val mutableAllRequiredFieldFilled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val allRequiredFieldFilled = mutableAllRequiredFieldFilled.asStateFlow()

    private var autoValidationJob: Job? = null

    private val mutableValidationInProgress = MutableStateFlow(false)
    override val validationInProgress = mutableValidationInProgress.asStateFlow()

    init {
        checkAllRequiredFieldFilled()
    }

    private fun launchAutoValidation(validationBlock: suspend () -> Unit) {
        val context: CoroutineContext = autoValidationExceptionHandler ?: EmptyCoroutineContext
        val coroutineScope: CoroutineScope = autoValidationScope
            ?: error("Should provide a validation scope for auto validation strategies!")

        autoValidationJob?.cancel()
        autoValidationJob = coroutineScope.launch(context) {
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
        val fieldItem = FormFieldItemImpl(
            fieldId = formField.id,
            fieldState = getFieldStateFlow<T>(formField.id)
        )
        return fieldItem.also { fieldItem.setListener(this) }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> getFieldStateFlow(id: String): MutableStateFlow<FormFieldState<T>> {
        return requireNotNull(fieldStates[id] as MutableStateFlow<FormFieldState<T>>)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getFieldItem(id: String): FormFieldItem<T> {
        return requireNotNull(fieldItems[id] as FormFieldItem<T>)
    }

    override fun onFieldFocusCleared(id: String) {
        if (validationStrategy == FormFieldValidationStrategy.AUTO_ON_FOCUS_CLEAR) {
            launchAutoValidation {
                validateAndUpdateFieldState(id)
            }
        }
    }

    override fun <T> onFieldValueChanged(id: String, value: T?) {
        val currentFieldState = getFieldStateFlow<T>(id)
        currentFieldState.value = currentFieldState.value.copy(
            value = value,
            validationResult = null // Clear validation result on value change
        )
        if (requiredFieldIds.contains(id)) {
            checkAllRequiredFieldFilled()
        }
        if (validationStrategy == FormFieldValidationStrategy.AUTO_INLINE) {
            launchAutoValidation {
                validateAndUpdateFieldState(id)
            }
        }
    }

    private fun checkAllRequiredFieldFilled() {
        mutableAllRequiredFieldFilled.value = requiredFieldIds
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
        var isFormValid = false

        monitorValidationProgress {
            isFormValid = fieldStates.keys
                .map { id -> validateAndUpdateFieldState(id) }
                .all { isValid -> isValid }
        }

        return isFormValid
    }

    private suspend fun validateAndUpdateFieldState(id: String): Boolean {
        val fieldState = fieldStates.getValue(id)
        val validationResult = validateField(
            fieldValue = fieldState.value.value,
            validators = getValidators(id)
        )
        fieldState.value = fieldState.value.copy(validationResult = validationResult)
        return validationResult is FormFieldValidationResult.Valid
    }

    private suspend fun <T> validateField(
        fieldValue: T?,
        validators: List<FormFieldValidator<T>>
    ): FormFieldValidationResult {
        validators.forEach { validator ->
            val result = validator.validate(value = fieldValue)
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

    override fun setFormInvalid() {
        fieldStates.forEach { (_, fieldItemState) ->
            fieldItemState.value = fieldItemState.value.copy(
                validationResult = FormFieldValidationResult.Invalid.Unknown
            )
        }
    }

    override fun clearForm() {
        formFields.forEach { formField ->
            fieldStates[formField.id]?.value = formField.initialState
        }
        checkAllRequiredFieldFilled()
    }
}
