package com.icell.external.carlosformito.api

import com.icell.external.carlosformito.api.model.FormFieldState
import kotlinx.coroutines.flow.StateFlow

interface FormFieldItem<T> {

    val fieldState: StateFlow<FormFieldState<T>>

    fun <T> onFieldValueChanged(value: T?)

    fun onFieldFocusCleared()
}
