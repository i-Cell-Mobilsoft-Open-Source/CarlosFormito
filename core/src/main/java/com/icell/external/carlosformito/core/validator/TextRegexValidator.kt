package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class TextRegexValidator(
    private val pattern: String,
    @StringRes
    private val errorMessageId: Int? = null
) : FormFieldValidator<String> {

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
