package com.icell.external.carlosformito.core.api

import com.icell.external.carlosformito.core.api.model.FormFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

interface FormFieldItem<T> {

    val fieldState: StateFlow<FormFieldState<T>>

    fun <T> onFieldValueChanged(value: T?)

    fun onFieldFocusCleared()

    fun onFieldVisibilityChanged(visible: Boolean)
}

val <T> FormFieldItem<T>.valueState: Flow<T?>
    get() = fieldState.map { state -> state.value }
