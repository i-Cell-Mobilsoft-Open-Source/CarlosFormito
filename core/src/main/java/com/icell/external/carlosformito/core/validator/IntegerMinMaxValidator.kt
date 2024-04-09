package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class IntegerMinMaxValidator(
    private val minValue: Int,
    private val maxValue: Int,
    @StringRes
    private val errorMessageId: Int? = null
) : FormFieldValidator<Int> {

    override suspend fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value !in minValue..maxValue) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue, maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
