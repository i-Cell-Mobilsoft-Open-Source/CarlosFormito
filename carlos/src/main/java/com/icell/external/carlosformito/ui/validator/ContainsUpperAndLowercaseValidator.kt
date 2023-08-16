package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.api.validator.FormFieldValidator

class ContainsUpperAndLowercaseValidator(
    @StringRes private val errorMessageId: Int
) : FormFieldValidator<String> {

    override suspend fun validate(value: String?): FormFieldValidationResult {
        val nonNullValue = value ?: ""
        return if (nonNullValue.none { char -> char.isUpperCase() } ||
            nonNullValue.none { char -> char.isLowerCase() }
        ) {
            FormFieldValidationResult.Invalid.Message(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
