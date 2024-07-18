package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `IntegerMinValidator` validates that an Integer value is not less than a specified minimum value.
 *
 * @param minValue The minimum Integer value that the validated value should not be less than.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class IntegerMinValidator(
    private val minValue: Int,
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<Int> {

    /**
     * Validates the given Integer value.
     *
     * @param value The Integer value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (not less than the minimum value),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value < minValue) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
