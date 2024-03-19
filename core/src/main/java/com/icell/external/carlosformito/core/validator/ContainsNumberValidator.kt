package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class ContainsNumberValidator(
    @StringRes private val errorMessageId: Int
) : FormFieldValidator<String> {

    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value.orEmpty()
        return if (nonNullValue.none { char -> char.isDigit() }) {
            FormFieldValidationResult.Invalid.Message(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
