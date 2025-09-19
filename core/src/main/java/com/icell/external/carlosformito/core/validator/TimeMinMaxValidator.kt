package com.icell.external.carlosformito.core.validator

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalTime

/**
 * `TimeMinMaxValidator` validates that a LocalTime value is within a specified range.
 *
 * @param minValue The minimum LocalTime value that the validated value should not be before.
 * @param maxValue The maximum LocalTime value that the validated value should not be after.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class TimeMinMaxValidator(
    private val minValue: LocalTime,
    private val maxValue: LocalTime,
    private val errorMessageId: Int? = null
) : FormFieldValidator<LocalTime> {

    /**
     * Validates the given LocalTime value.
     *
     * @param value The LocalTime value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (within the specified range),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: LocalTime?): FormFieldValidationResult {
        value?.let {
            if (value.isBefore(minValue) || value.isAfter(maxValue)) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue, maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
