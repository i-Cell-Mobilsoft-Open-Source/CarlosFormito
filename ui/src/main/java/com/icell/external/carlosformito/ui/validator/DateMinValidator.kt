package com.icell.external.carlosformito.ui.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.ui.R
import java.time.LocalDate

class DateMinValidator(
    private val minValue: LocalDate,
    @StringRes private val errorMessageId: Int = R.string.carlos_lbl_validator_date_min_error
) : FormFieldValidator<LocalDate> {

    override suspend fun validate(value: LocalDate?): FormFieldValidationResult {
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
