package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `IntegerMinMaxValidator` validates that an Integer value is within a specified range.
 *
 * @param minValue The minimum Integer value that the validated value should not be less than.
 * @param maxValue The maximum Integer value that the validated value should not be greater than.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class IntegerMinMaxValidator(
    private val minValue: Int,
    private val maxValue: Int,
    @StringRes
    private val errorMessageId: Int? = null
) : FormFieldValidator<Int> {

    /**
     * Validates the given Integer value against the specified range.
     *
     * @param value The Integer value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (within the specified range),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value !in minValue..maxValue) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue, maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
