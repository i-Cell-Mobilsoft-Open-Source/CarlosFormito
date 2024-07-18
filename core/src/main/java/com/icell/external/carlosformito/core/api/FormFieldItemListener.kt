package com.icell.external.carlosformito.core.api

/**
 * Interface for listening to events from [FormFieldItem]s.
 */
interface FormFieldItemListener {

    /**
     * Notifies when focus is cleared from a [FormFieldItem].
     *
     * @param id The unique identifier of the form field.
     */
    fun onFieldFocusCleared(id: String)

    /**
     * Notifies when the value of a form field changes.
     *
     * @param id The unique identifier of the form field.
     * @param value The new value of the form field.
     */
    fun <T> onFieldValueChanged(id: String, value: T?)

    /**
     * Notifies when the visibility of a form field changes.
     *
     * @param id The unique identifier of the form field.
     * @param visible Boolean indicating whether the form field is visible or not.
     */
    fun onFieldVisibilityChanged(id: String, visible: Boolean)
}
