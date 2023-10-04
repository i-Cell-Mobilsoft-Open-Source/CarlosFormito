package com.icell.external.carlosformito.demo.ui.custom.fields.validator

import com.icell.external.carlosformito.core.api.validator.FormFieldValidationResult
import com.icell.external.carlosformito.core.api.validator.FormFieldValidator
import com.icell.external.carlosformito.demo.R
import com.icell.external.carlosformito.demo.ui.custom.fields.model.ValidityStart
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ValidityStartValidator(
    private val minDateTime: ZonedDateTime,
    private val maxDateTime: ZonedDateTime
) : FormFieldValidator<ValidityStart> {

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd hh:mm")

    override suspend fun validate(value: ValidityStart?): FormFieldValidationResult {
        value?.let {
            val nonNullDate = value.date ?: return FormFieldValidationResult.Invalid.Message(
                R.string.validity_start_missing_date
            )
            val nonNullTime = value.time ?: return FormFieldValidationResult.Invalid.Message(
                R.string.validity_start_missing_time
            )
            val dateTimeValue = ZonedDateTime.of(nonNullDate, nonNullTime, ZoneId.systemDefault())
            if (dateTimeValue.isBefore(minDateTime) || dateTimeValue.isAfter(maxDateTime)) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    R.string.validity_start_out_of_range,
                    listOf(
                        minDateTime.format(dateTimeFormatter),
                        maxDateTime.format(dateTimeFormatter)
                    )
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
