package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

/**
 * `TextRegexValidator` validates that a String value matches a specified regular expression pattern.
 *
 * @param pattern The regular expression pattern against which the value is validated.
 * @param errorMessageId Optional resource ID for an error message to be displayed if validation fails.
 */
class TextRegexValidator(
    private val pattern: String,
    @StringRes
    private val errorMessageId: Int? = null
) : FormFieldValidator<String> {

    /**
     * Validates the given String value against the specified regular expression pattern.
     *
     * @param value The String value to be validated.
     * @return [FormFieldValidationResult.Valid] if the value matches the pattern or is empty,
     *         [FormFieldValidationResult.Invalid] otherwise.
     */
    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value.orEmpty()
        if (nonNullValue.isEmpty()) {
            return FormFieldValidationResult.Valid
        }
        if (!pattern.toRegex().matches(nonNullValue)) {
            return FormFieldValidationResult.Invalid.of(errorMessageId)
        }
        return FormFieldValidationResult.Valid
    }
}
