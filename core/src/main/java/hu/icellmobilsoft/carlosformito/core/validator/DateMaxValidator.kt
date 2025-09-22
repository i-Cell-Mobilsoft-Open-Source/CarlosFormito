package hu.icellmobilsoft.carlosformito.core.validator

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalDate

/**
 * `DateMaxValidator` validates that a LocalDate value is not after a specified maximum date.
 *
 * @param maxValue The maximum LocalDate value that the validated value should not be after.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class DateMaxValidator(
    private val maxValue: LocalDate,
    private val errorMessageId: Int? = null
) : FormFieldValidator<LocalDate> {

    /**
     * Validates the given LocalDate value.
     *
     * @param value The LocalDate value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (not after the maximum date),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: LocalDate?): FormFieldValidationResult {
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
