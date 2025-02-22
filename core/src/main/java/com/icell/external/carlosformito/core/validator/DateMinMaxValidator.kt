package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalDate

/**
 * `DateMinMaxValidator` validates that a LocalDate value is within a specified date range.
 *
 * @param minValue The minimum LocalDate value that the validated value should not be before.
 * @param maxValue The maximum LocalDate value that the validated value should not be after.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class DateMinMaxValidator(
    private val minValue: LocalDate,
    private val maxValue: LocalDate,
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<LocalDate> {

    /**
     * Validates the given LocalDate value against the specified date range.
     *
     * @param value The LocalDate value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (within the specified date range),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: LocalDate?): FormFieldValidationResult {
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
