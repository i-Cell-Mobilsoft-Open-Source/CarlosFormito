package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.ui.R

class TextRegexValidator(
    private val pattern: String,
    @StringRes
    private val errorMessageId: Int = R.string.formular_lbl_validator_validator_value_invalid_format
) : FormFieldValidator<String> {

    override fun validate(value: String?): FormFieldValidationResult {
        if (!pattern.toRegex().matches(value ?: "")) {
            return FormFieldValidationResult.Invalid.Message(errorMessageId)
        }
        return FormFieldValidationResult.Valid
    }
}
