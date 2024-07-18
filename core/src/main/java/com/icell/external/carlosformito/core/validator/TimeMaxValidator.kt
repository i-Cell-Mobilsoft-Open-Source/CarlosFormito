package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalTime

/**
 * `TimeMaxValidator` validates that a LocalTime value is not after a specified maximum value.
 *
 * @param maxValue The maximum LocalTime value that the validated value should not be after.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class TimeMaxValidator(
    private val maxValue: LocalTime,
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<LocalTime> {

    /**
     * Validates the given LocalTime value.
     *
     * @param value The LocalTime value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (not after the maximum value),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: LocalTime?): FormFieldValidationResult {
        value?.let {
            if (value.isAfter(maxValue)) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
