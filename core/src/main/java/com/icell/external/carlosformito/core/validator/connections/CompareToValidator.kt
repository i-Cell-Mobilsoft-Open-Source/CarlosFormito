package com.icell.external.carlosformito.core.validator.connections

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormValueContext

/**
 * A validator that compares the value of a form field against another connected field's value.
 *
 * @param T The type of the field values to be compared.
 * @property connectedFieldId The ID of the connected field to compare against.
 * @property errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 * @property compare A lambda function that defines the comparison logic between the current field's value
 *                   and the connected field's value. Return `true` if the values should pass the validation
 *                   and `false` otherwise.
 */
class CompareToValidator<T>(
    override val connectedFieldId: String,
    private val errorMessageId: Int? = null,
    private val compare: (fieldValue: T, connectedFieldValue: T) -> Boolean
) : ConnectionValidator<T>() {

    /**
     * Validates the current field's value by comparing it to the connected field's value.
     *
     * @param value The value of the current form field.
     * @param context The context containing the values of other form fields.
     * @return A [FormFieldValidationResult] indicating whether the validation was successful.
     */
    override suspend fun validate(value: T?, context: FormValueContext): FormFieldValidationResult {
        val fieldValue = value ?: return FormFieldValidationResult.Valid
        val connectedFieldValue = context.getFieldValue<T>(connectedFieldId) ?: return FormFieldValidationResult.Valid

        return if (!compare(fieldValue, connectedFieldValue)) {
            FormFieldValidationResult.Invalid.of(errorMessageId, listOf(connectedFieldValue))
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
