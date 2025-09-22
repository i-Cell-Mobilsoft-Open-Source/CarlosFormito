package hu.icellmobilsoft.carlosformito.core.validator

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalTime

/**
 * `TimeMinValidator` validates that a LocalTime value is not before a specified minimum value.
 *
 * @param minValue The minimum LocalTime value that the validated value should not be before.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class TimeMinValidator(
    private val minValue: LocalTime,
    private val errorMessageId: Int? = null
) : FormFieldValidator<LocalTime> {

    /**
     * Validates the given LocalTime value.
     *
     * @param value The LocalTime value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (not before the minimum value),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: LocalTime?): FormFieldValidationResult {
        value?.let {
            if (value.isBefore(minValue)) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
