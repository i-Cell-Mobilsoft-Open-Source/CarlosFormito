package com.icell.external.carlosformito.core.api

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.StateFlow

interface FormManager : FormFieldItemListener {

    val allRequiredFieldFilled: StateFlow<Boolean>

    val validationInProgress: StateFlow<Boolean>

    suspend fun initFormManager(autoValidationExceptionHandler: CoroutineExceptionHandler? = null)

    fun <T> getFieldItem(id: String): FormFieldItem<T>

    suspend fun validateForm(): Boolean

    fun setFormInvalid()

    fun clearForm()

    fun printFormState()
}
