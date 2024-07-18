package com.icell.external.carlosformito.core.api.model

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult

/**
 * Represents the state of a form field, including its current value and validation result.
 *
 * @property value The current value of the form field.
 * @property validationResult The result of the form field's validation.
 */
data class FormFieldState<T>(
    val value: T? = null,
    val validationResult: FormFieldValidationResult? = null
) {

    /**
     * Checks if the form field is in an error state based on its validation result.
     */
    val isError: Boolean
        get() = validationResult is FormFieldValidationResult.Invalid
}
