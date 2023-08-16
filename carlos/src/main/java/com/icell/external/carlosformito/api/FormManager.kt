package com.icell.external.carlosformito.api

import kotlinx.coroutines.flow.StateFlow

interface FormManager {

    val allRequiredFieldFilled: StateFlow<Boolean>

    fun <T> getFieldItem(id: String): FormFieldItem<T>

    suspend fun validateForm(): Boolean

    fun setFormInvalid()
}
