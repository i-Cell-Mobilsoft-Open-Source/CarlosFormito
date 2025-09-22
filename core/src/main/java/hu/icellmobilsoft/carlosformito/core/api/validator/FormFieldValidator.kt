package hu.icellmobilsoft.carlosformito.core.api.validator

/**
 * Interface for defining validators for form field values.
 *
 * @param T The type of value that this validator can validate.
 */
interface FormFieldValidator<T> : IsFormFieldValidator<T> {
    /**
     * Validates the given value of type T.
     *
     * @param value The value to validate.
     * @return [FormFieldValidationResult] representing the validation result.
     */
    suspend fun validate(value: T?): FormFieldValidationResult
}
