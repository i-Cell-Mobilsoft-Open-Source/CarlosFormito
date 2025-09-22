package hu.icellmobilsoft.carlosformito.core.api

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
     * Notifies the form field item that its value has been reset to the initial value.
     *
     * @param id The unique identifier of the form field.
     */
    fun onFieldValueReset(id: String)

    /**
     * Notifies when the visibility of a form field changes.
     *
     * @param id The unique identifier of the form field.
     * @param visible Boolean indicating whether the form field is visible or not.
     */
    fun onFieldVisibilityChanged(id: String, visible: Boolean)

    /**
     * Validates the form field item associated with the given ID.
     *
     * @param id The unique identifier of the form field item.
     * @return `true` if the field is valid after validation, `false` otherwise.
     */
    suspend fun validateField(id: String): Boolean
}
