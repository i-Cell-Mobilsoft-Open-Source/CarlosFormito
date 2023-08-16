package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.R
import com.icell.external.carlosformito.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.api.validator.FormFieldValidator
import java.time.LocalTime

class TimeMinValidator(
    private val minValue: LocalTime,
    @StringRes private val errorMessageId: Int = R.string.carlos_lbl_validator_time_min_error
) : FormFieldValidator<LocalTime> {

    override suspend fun validate(value: LocalTime?): FormFieldValidationResult {
        value?.let {
            if (value.isBefore(minValue)) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
