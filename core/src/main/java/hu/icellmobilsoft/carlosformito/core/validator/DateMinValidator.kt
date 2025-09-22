package hu.icellmobilsoft.carlosformito.core.validator

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalDate

/**
 * `DateMinValidator` validates that a LocalDate value is not before a specified minimum date.
 *
 * @param minValue The minimum LocalDate value that the validated value should not be before.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class DateMinValidator(
    private val minValue: LocalDate,
    private val errorMessageId: Int? = null
) : FormFieldValidator<LocalDate> {

    /**
     * Validates the given LocalDate value.
     *
     * @param value The LocalDate value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (not before the minimum date),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: LocalDate?): FormFieldValidationResult {
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
