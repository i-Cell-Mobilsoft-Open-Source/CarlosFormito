package com.icell.external.carlosformito.core.api.model

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult

/**
 * Represents the state of a form field, including its current value and validation result.
 *
 * @property value The current value entered in the form field. Defaults to `null` if no input is provided yet.
 * @property validationInProgress A flag indicating whether the field is currently being validated
 * (e.g., for async validation such as checking username availability).
 * @property validationResult The latest validation result for the field, or `null` if no validation
 * has been performed yet.
 */
data class FormFieldState<T>(
    val value: T? = null,
    val validationInProgress: Boolean = false,
    val validationResult: FormFieldValidationResult? = null
) {

    /**
     * Checks if the form field is in an error state based on its validation result.
     */
    val isError: Boolean
        get() = validationResult is FormFieldValidationResult.Invalid

    /**
     * Checks if the form field is filled.
     */
    val isFilled: Boolean
        get() {
            return if (value is String) {
                value.isNotBlank()
            } else {
                value != null
            }
        }
}
