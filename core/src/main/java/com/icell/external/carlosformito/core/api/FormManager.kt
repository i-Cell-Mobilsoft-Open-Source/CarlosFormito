package com.icell.external.carlosformito.core.api

import com.icell.external.carlosformito.core.api.model.FormFieldState
import kotlinx.coroutines.flow.StateFlow

interface FormManager {

    val allRequiredFieldFilled: StateFlow<Boolean>

    fun <T> getFieldStateFlow(id: String): StateFlow<FormFieldState<T>>

    fun <T> getFieldHandle(id: String): FormFieldHandle<T>

    fun validateForm(): Boolean

    fun setFormInvalid()
}
