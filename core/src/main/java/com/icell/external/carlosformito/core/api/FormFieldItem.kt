package com.icell.external.carlosformito.core.api

import com.icell.external.carlosformito.core.api.model.FormFieldState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map

/**
 * Interface representing a form field item.
 *
 * This interface facilitates communication between the user interface and the form manager:
 * 1. Provides a state flow of field value and validation results
 * 2. Delivers value changes, focus clear and visibility change events
 *
 * @param T The type of value associated with the form field item.
 */
interface FormFieldItem<T> {

    /**
     * The state flow representing the current state of the form field.
     */
    val fieldState: StateFlow<FormFieldState<T>>

    /**
     * Notifies the form field item that its value has changed.
     *
     * @param value The new value of the form field.
     */
    fun <T> onFieldValueChanged(value: T?)

    /**
     * Notifies the form field item that its value has been reset to the initial value.
     */
    fun onFieldValueReset()

    /**
     * Notifies the form field item that focus has been cleared from the field.
     */
    fun onFieldFocusCleared()

    /**
     * Notifies the form field item about changes in field visibility.
     *
     * @param visible Boolean indicating whether the field is visible or not.
     */
    fun onFieldVisibilityChanged(visible: Boolean)

    /**
     * Validates the form field item.
     *
     * @return `true` if the field is valid after validation, `false` otherwise.
     */
    suspend fun validateField(): Boolean
}

/**
 * Extension property to obtain a flow of the current value of the form field item.
 *
 * @param T The type of value associated with the form field item.
 * @return Flow emitting the current value of the form field item.
 */
val <T> FormFieldItem<T>.valueState: Flow<T?>
    get() = fieldState.map { state -> state.value }
