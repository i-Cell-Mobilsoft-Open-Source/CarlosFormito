package com.icell.external.carlosformito.ui.util

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime

object TimePickerBuilder {

    fun build(
        dialogTitle: String,
        selectedTime: LocalTime? = null,
        timeFormat: Int = TimeFormat.CLOCK_24H,
        onTimeSelected: (LocalTime) -> Unit
    ): MaterialTimePicker {
        val timePickerBuilder =
            MaterialTimePicker.Builder()
                .setTimeFormat(timeFormat)
                .setHour(selectedTime?.hour ?: 0)
                .setMinute(selectedTime?.minute ?: 0)
                .setTitleText(dialogTitle)

        return timePickerBuilder.build().also { picker ->
            picker.addOnPositiveButtonClickListener {
                onTimeSelected(LocalTime.of(picker.hour, picker.minute))
            }
        }
    }
}
