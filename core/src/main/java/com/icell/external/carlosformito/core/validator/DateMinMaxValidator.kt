package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.R
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalDate

class DateMinMaxValidator(
    private val minValue: LocalDate,
    private val maxValue: LocalDate,
    @StringRes private val errorMessageId: Int = R.string.carlos_lbl_validator_date_min_max_error
) : FormFieldValidator<LocalDate> {

    override suspend fun validate(value: LocalDate?): FormFieldValidationResult {
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
