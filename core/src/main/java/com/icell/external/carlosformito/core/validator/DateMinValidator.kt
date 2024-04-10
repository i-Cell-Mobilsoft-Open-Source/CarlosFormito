package com.icell.external.carlosformito.core.validator

import androidx.annotation.StringRes
import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import java.time.LocalDate

class DateMinValidator(
    private val minValue: LocalDate,
    @StringRes private val errorMessageId: Int? = null
) : FormFieldValidator<LocalDate> {

    override suspend fun validate(value: LocalDate?): FormFieldValidationResult {
        value?.let {
            if (value.isBefore(minValue)) {
                return FormFieldValidationResult.Invalid.of(
                    errorMessageId = errorMessageId,
                    formatArgs = listOf(minValue)
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
