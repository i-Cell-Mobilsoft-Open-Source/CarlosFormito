package hu.icellmobilsoft.carlosformito.core.validator

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import kotlinx.datetime.LocalTime

/**
 * `TimeMaxValidator` validates that a LocalTime value is not after a specified maximum value.
 *
 * @param maxValue The maximum LocalTime value that the validated value should not be after.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class TimeMaxValidator(
    private val maxValue: LocalTime,
    private val errorMessageId: Int? = null
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
