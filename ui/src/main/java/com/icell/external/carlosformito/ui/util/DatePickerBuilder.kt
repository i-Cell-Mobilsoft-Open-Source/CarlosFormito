package com.icell.external.carlosformito.ui.util

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset

object DatePickerBuilder {

    private val ABSOLUTE_MIN_DATE: LocalDate = LocalDate.of(1900, 1, 1)

    fun build(
        dialogTitle: String,
        minDate: LocalDate? = null,
        maxDate: LocalDate? = null,
        selectedDate: LocalDate? = null,
        onDateSelected: (LocalDate) -> Unit,
        absoluteMinDate: LocalDate = ABSOLUTE_MIN_DATE
    ): MaterialDatePicker<Long> {
        val constraintsBuilder = CalendarConstraints.Builder()
        val dateValidators = mutableListOf<CalendarConstraints.DateValidator>()

        val minDateUtcEpoch = localDateToUtcMillis(minDate ?: absoluteMinDate)
        constraintsBuilder.setStart(minDateUtcEpoch)
        dateValidators.add(DateValidatorPointForward.from(minDateUtcEpoch))

        maxDate?.let {
            val maxDateUtcEpoch = localDateToUtcMillis(maxDate)
            constraintsBuilder.setEnd(maxDateUtcEpoch)
            dateValidators.add(DateValidatorPointBackward.before(maxDateUtcEpoch))
        }

        val calendarConstraints = constraintsBuilder
            .setValidator(CompositeDateValidator.allOf(dateValidators))
            .build()

        val datePickerBuilder = MaterialDatePicker.Builder.datePicker()
            .setTitleText(dialogTitle)
            .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
            .setCalendarConstraints(calendarConstraints)

        selectedDate?.let {
            val selectedDateUtc = localDateToUtcMillis(selectedDate)
            datePickerBuilder.setSelection(selectedDateUtc)
        }

        return datePickerBuilder.build().also { picker ->
            picker.addOnPositiveButtonClickListener { utcMillis ->
                onDateSelected(localDateFromUtcMillis(utcMillis))
            }
        }
    }

    private fun localDateToUtcMillis(localDate: LocalDate): Long {
        return localDate.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
    }

    private fun localDateFromUtcMillis(utcMillis: Long): LocalDate {
        return Instant.ofEpochMilli(utcMillis).atZone(ZoneId.systemDefault()).toLocalDate()
    }
}
