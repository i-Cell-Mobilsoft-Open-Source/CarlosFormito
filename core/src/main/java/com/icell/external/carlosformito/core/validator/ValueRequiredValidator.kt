package com.icell.external.carlosformito.core.validator

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `ValueRequiredValidator` validates that a value is not null or blank.
 *
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 * @param T The type of the value being validated.
 */
class ValueRequiredValidator<T>(
    private val errorMessageId: Int? = null
) : FormFieldValidator<T> {

    /**
     * Validates the given value.
     *
     * @param value The value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (not null or empty),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: T?): FormFieldValidationResult {
        return if (value == null || value is String && value.isBlank()) {
            FormFieldValidationResult.Invalid.of(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
