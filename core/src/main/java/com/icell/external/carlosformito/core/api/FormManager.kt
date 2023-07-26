package com.icell.external.carlosformito.core.api

import kotlinx.coroutines.flow.StateFlow

interface FormManager {

    val allRequiredFieldFilled: StateFlow<Boolean>

    fun <T> getFieldItem(id: String): FormFieldItem<T>

    fun validateForm(): Boolean

    fun setFormInvalid()
}
