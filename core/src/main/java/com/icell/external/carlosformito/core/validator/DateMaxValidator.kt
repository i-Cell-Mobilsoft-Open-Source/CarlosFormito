package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalDate

class DateMaxValidator(
    private val maxValue: LocalDate,
    @StringRes private val errorMessageId: Int = R.string.carlos_lbl_validator_date_max_error
) : FormFieldValidator<LocalDate> {

    override suspend fun validate(value: LocalDate?): FormFieldValidationResult {
        value?.let {
            if (value.isAfter(maxValue)) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
