package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class IntegerMinValidator(
    private val minValue: Int,
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<Int> {

    override suspend fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value < minValue) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
