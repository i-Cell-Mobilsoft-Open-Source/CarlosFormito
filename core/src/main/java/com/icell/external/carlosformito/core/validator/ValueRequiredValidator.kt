package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class ValueRequiredValidator<T>(
    @StringRes
    private val errorMessageId: Int? = null
) : FormFieldValidator<T> {

    override suspend fun validate(value: T?): FormFieldValidationResult {
        return if (value == null || value is String && value.isBlank()) {
            FormFieldValidationResult.Invalid.of(errorMessageId)
        } else {
            FormFieldValidationResult.Valid
        }
    }
}
