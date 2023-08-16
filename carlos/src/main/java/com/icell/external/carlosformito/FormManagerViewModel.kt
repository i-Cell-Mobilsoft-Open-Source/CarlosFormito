package com.icell.external.carlosformito

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.icell.external.carlosformito.api.FormFieldItem
import com.icell.external.carlosformito.api.FormFieldItemListener
import com.icell.external.carlosformito.api.FormManager
import com.icell.external.carlosformito.api.model.FormField
import com.icell.external.carlosformito.api.model.FormFieldState
import com.icell.external.carlosformito.api.model.FormFieldValidationStrategy
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.api.validator.FormFieldValidator
import com.icell.external.carlosformito.api.validator.RequiresFieldValue
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@Suppress("TooManyFunctions")
open class FormManagerViewModel(
    private val formFields: List<FormField<*>>,
    private val validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.MANUAL,
    protected var validationExceptionHandler: CoroutineExceptionHandler? = null
) : ViewModel(), FormManager, FormFieldItemListener {

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
        .filterRequiredFields()
        .map { formField -> formField.id }

    private val mutableAllRequiredFieldFilled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val allRequiredFieldFilled = mutableAllRequiredFieldFilled.asStateFlow()

    private var validationJob: Job? = null

    private val mutableValidationInProgress = MutableStateFlow(false)
    val validationInProgress = mutableValidationInProgress.asStateFlow()

    init {
        checkAllRequiredFieldFilled()
    }

    private fun launchValidation(validationBlock: suspend () -> Unit) {
        val context: CoroutineContext = validationExceptionHandler ?: EmptyCoroutineContext

        validationJob?.cancel()
        validationJob = viewModelScope.launch(context) {
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
        if (validationStrategy == FormFieldValidationStrategy.ON_FOCUS_CLEAR) {
            launchValidation {
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
        if (validationStrategy == FormFieldValidationStrategy.INLINE) {
            launchValidation {
                validateAndUpdateFieldState(id)
            }
        }
    }

    private fun List<FormField<*>>.filterRequiredFields(): List<FormField<*>> {
        return filter { formField ->
            formField.validators.any { validator -> validator is RequiresFieldValue }
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
}
