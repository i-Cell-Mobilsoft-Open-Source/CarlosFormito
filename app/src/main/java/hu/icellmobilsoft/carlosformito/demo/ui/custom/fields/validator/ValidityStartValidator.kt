package hu.icellmobilsoft.carlosformito.demo.ui.custom.fields.validator

import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidationResult
import hu.icellmobilsoft.carlosformito.core.api.validator.FormFieldValidator
import hu.icellmobilsoft.carlosformito.demo.R
import hu.icellmobilsoft.carlosformito.demo.ui.custom.fields.model.ValidityStart
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format

class ValidityStartValidator(
    private val minDateTime: LocalDateTime,
    private val maxDateTime: LocalDateTime
) : FormFieldValidator<ValidityStart> {

    private val dateTimeFormat = LocalDateTime.Formats.ISO

    override suspend fun validate(value: ValidityStart?): FormFieldValidationResult {
        value?.let {
            val nonNullDate = value.date ?: return FormFieldValidationResult.Invalid.Message(
                R.string.validity_start_missing_date
            )
            val nonNullTime = value.time ?: return FormFieldValidationResult.Invalid.Message(
                R.string.validity_start_missing_time
            )
            val dateTimeValue = LocalDateTime(nonNullDate, nonNullTime)
            if (dateTimeValue < minDateTime || dateTimeValue > maxDateTime) {
                return FormFieldValidationResult.Invalid.MessageWithArgs(
                    R.string.validity_start_out_of_range,
                    listOf(
                        minDateTime.format(dateTimeFormat),
                        maxDateTime.format(dateTimeFormat)
                    )
                )
            }
        }
        return FormFieldValidationResult.Valid
    }
}
