package hu.icellmobilsoft.carlosformito.core.api.validator

/**
 * Interface for defining validators for form field values in the context of other form field values.
 *
 * @param T The type of value associated with the form field to be validated.
 */
interface CrossFormFieldValidator<T> : IsFormFieldValidator<T> {

    /**
     * Validates the given value in the context of the form.
     *
     * @param value The value to validate.
     * @param context The context of the form to access other form field values.
     * @return [FormFieldValidationResult] representing the validation result.
     */
    suspend fun validate(value: T?, context: FormValueContext): FormFieldValidationResult
}
