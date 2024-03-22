package com.icell.external.carlosformito.core.api

import com.icell.external.carlosformito.core.api.model.FormFieldState
import kotlinx.coroutines.flow.StateFlow

interface FormFieldItem<T> {

    val fieldState: StateFlow<FormFieldState<T>>

    fun <T> onFieldValueChanged(value: T?)

    fun onFieldFocusCleared()

    fun onFieldVisibilityChanged(visible: Boolean)
}
