package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `ContainsUpperAndLowercaseValidator` validates that a String value contains both uppercase and lowercase characters.
 *
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class ContainsUpperAndLowercaseValidator(
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<String> {

    /**
     * Validates the given String value.
     *
     * @param value The String value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value is valid (contains both upper and lowercase characters),
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value.orEmpty()
        if (nonNullValue.isEmpty()) {
            return FormFieldValidationResult.Valid
        }
        return if (
            nonNullValue.none { it.isUpperCase() } || nonNullValue.none { it.isLowerCase() }
        ) {
            FormFieldValidationResult.Invalid.of(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
