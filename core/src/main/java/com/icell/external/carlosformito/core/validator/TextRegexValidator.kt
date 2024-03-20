package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class TextRegexValidator(
    private val pattern: String,
    @StringRes
    private val errorMessageId: Int = R.string.carlos_lbl_validator_validator_value_invalid_format
) : FormFieldValidator<String> {

    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value.orEmpty()
        if (nonNullValue.isEmpty()) {
            return FormFieldValidationResult.Valid
        }
        if (!pattern.toRegex().matches(nonNullValue)) {
            return FormFieldValidationResult.Invalid.Message(errorMessageId)
        }
        return FormFieldValidationResult.Valid
    }
}
