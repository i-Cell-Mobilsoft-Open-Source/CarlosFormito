package com.icell.external.carlosformito.core.api

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import kotlinx.coroutines.flow.StateFlow

interface FormManager {

    val allRequiredFieldFilled: StateFlow<Boolean>

    fun <T> getFieldItem(id: String): FormFieldItem<T>

    suspend fun validateForm(): Boolean

    fun setFormInvalid()

    fun setFieldInvalid(id: String, invalidResult: FormFieldValidationResult.Invalid)
}
