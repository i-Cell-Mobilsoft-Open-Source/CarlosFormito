package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.api.validator.FormFieldValidator

class IntegerMinMaxValidator(
    private val minValue: Int,
    private val maxValue: Int,
    @StringRes
    private val errorMessageId: Int = R.string.carlos_lbl_validator_integer_min_max_error
) : FormFieldValidator<Int> {

    override suspend fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value !in minValue..maxValue) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue, maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
