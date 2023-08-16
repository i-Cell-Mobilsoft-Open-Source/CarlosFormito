package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.api.validator.FormFieldValidator
import java.time.LocalTime

class TimeMaxValidator(
    private val maxValue: LocalTime,
    @StringRes private val errorMessageId: Int = R.string.carlos_lbl_validator_time_max_error
) : FormFieldValidator<LocalTime> {

    override suspend fun validate(value: LocalTime?): FormFieldValidationResult {
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
