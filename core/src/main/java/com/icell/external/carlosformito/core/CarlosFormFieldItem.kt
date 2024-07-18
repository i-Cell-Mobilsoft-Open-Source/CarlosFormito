package com.icell.external.carlosformito.core

import com.icell.external.carlosformito.core.api.FormFieldItem
import com.icell.external.carlosformito.core.api.FormFieldItemListener
import com.icell.external.carlosformito.core.api.model.FormFieldState
import kotlinx.coroutines.flow.StateFlow

/**
 * An internal [FormFieldItem] implementation, encapsulating a form fields ID and state.
 *
 * Dispatches UI events of a field like value change, focus clear and visibility change to the form manager.
 *
 * @param fieldId The unique identifier of the form field.
 * @param fieldState The state flow representing the current state of the form field.
 * @param T The type of the form field value.
 */
internal class CarlosFormFieldItem<T>(
    private val fieldId: String,
    override val fieldState: StateFlow<FormFieldState<T>>
) : FormFieldItem<T> {

    private var listener: FormFieldItemListener? = null

    /**
     * Notifies the listener when the focus is cleared from the field.
     */
    override fun onFieldFocusCleared() {
        listener?.onFieldFocusCleared(fieldId)
    }

    /**
     * Notifies the listener when the field value changes.
     *
     * @param value The new value of the field.
     */
    override fun <T> onFieldValueChanged(value: T?) {
        listener?.onFieldValueChanged(fieldId, value)
    }

    /**
     * Notifies the listener when the visibility of the field changes.
     *
     * @param visible The new visibility state of the field.
     */
    override fun onFieldVisibilityChanged(visible: Boolean) {
        listener?.onFieldVisibilityChanged(fieldId, visible)
    }

    /**
     * Sets the listener to handle events related to this form field item.
     *
     * @param listener The listener to set.
     */
    fun setListener(listener: FormFieldItemListener?) {
        this.listener = listener
    }
}
