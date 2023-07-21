package com.icell.external.carlosformito.core

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.core.api.validator.RequiresFieldValue
import com.icell.external.carlosformito.core.api.model.FormFieldItem
import com.icell.external.carlosformito.core.api.model.FormFieldState
import com.icell.external.carlosformito.core.api.FormFieldHandle
import com.icell.external.carlosformito.core.api.FormFieldHandleCallback
import com.icell.external.carlosformito.core.api.FormManager
import com.icell.external.carlosformito.core.api.model.FormFieldValidationStrategy
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FormManagerImpl(
    private val formFields: List<FormFieldItem<*>>,
    private val validationStrategy: FormFieldValidationStrategy = FormFieldValidationStrategy.MANUAL
) : FormManager {

    private val fieldHandleCallback = object :
        FormFieldHandleCallback {
        override fun onFieldFocusCleared(id: String) {
            if (validationStrategy == FormFieldValidationStrategy.ON_FOCUS_CLEAR) {
                validateAndUpdateFieldState(id)
            }
        }

        override fun <T> onFieldValueChanged(id: String, value: T?) {
            this@FormManagerImpl.onFieldValueChanged(id, value)
            if (validationStrategy == FormFieldValidationStrategy.INLINE) {
                validateAndUpdateFieldState(id)
            }
        }
    }

    private val fieldStates: Map<String, MutableStateFlow<FormFieldState<*>>> =
        buildMap {
            formFields.forEach { formField ->
                put(formField.id, MutableStateFlow(formField.initialState))
            }
        }

    private val fieldHandles: Map<String, FormFieldHandle<*>> =
        buildMap {
            formFields.forEach { formField ->
                put(
                    formField.id,
                    createHandle(formField).also { handle ->
                        handle.setFieldHandleCallback(fieldHandleCallback)
                    }
                )
            }
        }

    private fun <T> createHandle(fieldItem: FormFieldItem<T>): FormFieldHandle<T> =
        FormFieldHandleImpl(fieldItem.id)

    private val requiredFieldIds: List<String> = formFields
        .filterRequiredFields()
        .map { formField -> formField.id }

    private val mutableAllRequiredFieldFilled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override val allRequiredFieldFilled: StateFlow<Boolean> = mutableAllRequiredFieldFilled

    init {
        checkAllRequiredFieldFilled()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getFieldStateFlow(id: String): MutableStateFlow<FormFieldState<T>> {
        return requireNotNull(fieldStates[id] as MutableStateFlow<FormFieldState<T>>)
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T> getFieldHandle(id: String): FormFieldHandle<T> {
        return requireNotNull(fieldHandles[id] as FormFieldHandle<T>)
    }

    private fun <T> onFieldValueChanged(id: String, value: T?) {
        val currentFieldState = getFieldStateFlow<T>(id)
        currentFieldState.value = currentFieldState.value.copy(
            value = value,
            validationResult = null // Clear validation result on value change
        )
        if (requiredFieldIds.contains(id)) {
            checkAllRequiredFieldFilled()
        }
    }

    private fun List<FormFieldItem<*>>.filterRequiredFields(): List<FormFieldItem<*>> {
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

    override fun validateForm(): Boolean {
        return fieldStates.keys
            .map { id -> validateAndUpdateFieldState(id) }
            .all { isValid -> isValid }
    }

    private fun validateAndUpdateFieldState(id: String): Boolean {
        val fieldState = fieldStates.getValue(id)
        val validationResult = validateField(
            fieldValue = fieldState.value.value,
            validators = getValidators(id)
        )
        fieldState.value = fieldState.value.copy(validationResult = validationResult)
        return validationResult is FormFieldValidationResult.Valid
    }

    private fun <T> validateField(
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
