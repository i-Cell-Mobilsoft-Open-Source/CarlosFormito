package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `ContainsNumberValidator` validates that a String value contains at least one numeric digit.
 *
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class ContainsNumberValidator(
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<String> {

    /**
     * Validates the given String value.
     *
     * @param value The String value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (contains at least one numeric digit),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value.orEmpty()
        if (nonNullValue.isEmpty()) {
            return FormFieldValidationResult.Valid
        }
        return if (nonNullValue.none { char -> char.isDigit() }) {
            FormFieldValidationResult.Invalid.of(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
