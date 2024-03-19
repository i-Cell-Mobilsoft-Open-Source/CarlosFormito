package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator

class IntegerMaxValidator(
    private val maxValue: Int,
    @StringRes private val errorMessageId: Int = R.string.carlos_lbl_validator_integer_max_error
) : FormFieldValidator<Int> {

    override suspend fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value > maxValue) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
