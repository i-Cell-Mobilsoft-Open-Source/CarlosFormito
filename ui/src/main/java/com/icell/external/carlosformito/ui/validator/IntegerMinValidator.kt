package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.ui.R

class IntegerMinValidator(
    private val minValue: Int,
    @StringRes private val errorMessageId: Int = R.string.formular_lbl_validator_integer_min_error
) : FormFieldValidator<Int> {

    override fun validate(value: Int?): FormFieldValidationResult {
        value?.let {
            if (value < minValue) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
