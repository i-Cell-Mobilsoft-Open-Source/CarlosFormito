package hu.icellmobilsoft.carlosformito.core.api.model

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.IsFormFieldValidator

/**
 * Represents a form field with an identifier, initial state, and validators.
 *
 * @property id The unique identifier of the form field.
 * @property initialValue The initial value of the form field
 * @property initialValidationResult The initial validation result of the form field
 * @property validators A list of validators used to validate the form field's value.
 * @property customValidationStrategy An optional custom field validation
 * strategy for defining custom validation behavior
 */
data class FormField<T>(
    val id: String,
    val initialValue: T? = null,
    val initialValidationResult: FormFieldValidationResult? = null,
    val validators: List<IsFormFieldValidator<T>> = emptyList(),
    val customValidationStrategy: FormFieldValidationStrategy? = null
) {
    val initialState = FormFieldState(
        value = initialValue,
        validationInProgress = false,
        validationResult = initialValidationResult
    )
}
