package com.icell.external.carlosformito.core.validator

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `IntegerMaxValidator` validates that an Integer value is not greater than a specified maximum value.
 *
 * @param maxValue The maximum Integer value that the validated value should not exceed.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class IntegerMaxValidator(
    private val maxValue: Int,
    private val errorMessageId: Int? = null
) : FormFieldValidator<Int> {

    /**
     * Validates the given Integer value.
     *
     * @param value The Integer value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (not greater than the maximum value),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value > maxValue) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
