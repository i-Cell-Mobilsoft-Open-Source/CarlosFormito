package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalTime

class TimeMaxValidator(
    private val maxValue: LocalTime,
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<LocalTime> {

    override suspend fun validate(value: LocalTime?): FormFieldValidationResult {
        value?.let {
            if (value.isAfter(maxValue)) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(maxValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
