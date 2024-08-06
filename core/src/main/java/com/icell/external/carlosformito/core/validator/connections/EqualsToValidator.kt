package com.icell.external.carlosformito.core.validator.connections

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormValueContext

/**
 * A validator that ensures a form field value equals to the value of another form field.
 *
 * @param T The type of the form field value.
 * @property connectedFieldId The ID of the form field whose value should equals.
 * @property errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class EqualsToValidator<T>(
    override val connectedFieldId: String,
    @StringRes private val errorMessageId: Int? = null
) : ConnectionValidator<T>() {

    /**
     * Validates that the value of the current form field equals to the value of the specified form field.
     *
     * @param value The value of the current form field.
     * @param context The context containing the values of other form fields.
     * @return A [FormFieldValidationResult] indicating whether the validation was successful.
     */
    override suspend fun validate(value: T?, context: FormValueContext): FormFieldValidationResult {
        val connectedFieldValue = context.getFieldValue<T>(connectedFieldId)

        return if (value != connectedFieldValue) {
            FormFieldValidationResult.Invalid.of(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
