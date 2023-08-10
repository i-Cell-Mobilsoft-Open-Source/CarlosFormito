package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.ui.R
import java.time.LocalTime

class TimeMinMaxValidator(
    private val minValue: LocalTime,
    private val maxValue: LocalTime,
    @StringRes private val errorMessageId: Int = R.string.carlos_lbl_validator_time_min_max_error
) : FormFieldValidator<LocalTime> {

    override suspend fun validate(value: LocalTime?): FormFieldValidationResult {
        value?.let {
            if (value.isBefore(minValue) || value.isAfter(maxValue)) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue, maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
