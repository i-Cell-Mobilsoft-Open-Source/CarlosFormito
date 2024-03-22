package com.icell.external.carlosformito.core.api

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface FormManager : FormFieldItemListener {

    val allRequiredFieldFilled: StateFlow<Boolean>

    var autoValidationScope: CoroutineScope?

    var autoValidationExceptionHandler: CoroutineExceptionHandler?

    val validationInProgress: StateFlow<Boolean>

    suspend fun initFormManager()

    fun <T> getFieldItem(id: String): FormFieldItem<T>

    suspend fun validateForm(): Boolean

    fun setFormInvalid()

    fun clearForm()

    fun printFormState()
}
