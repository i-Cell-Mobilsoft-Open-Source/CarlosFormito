package hu.icellmobilsoft.carlosformito.ui.util

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import kotlinx.datetime.LocalTime

/**
 * Object that provides a builder function for creating and configuring a [MaterialTimePicker].
 */
object TimePickerBuilder {

    /**
     * Builds a [MaterialTimePicker] dialog with the specified configuration.
     *
     * @param dialogTitle The title displayed at the top of the time picker dialog.
     * @param selectedTime The initial time shown in the time picker.
     * If `null`, the picker defaults to `00:00`. Defaults to `null`.
     * @param is24HourFormat Whether the time should be displayed in 24-hour or 12-hour format.
     * If `true`, [TimeFormat.CLOCK_24H] is used; otherwise [TimeFormat.CLOCK_12H].
     * Defaults to `true` (24-hour clock).
     * @param onTimeSelected Callback invoked when the user confirms a time selection.
     * The selected [LocalTime] is passed as an argument.
     *
     * @return A configured instance of [MaterialTimePicker].
     */
    fun build(
        dialogTitle: String,
        selectedTime: LocalTime? = null,
        is24HourFormat: Boolean = true,
        onTimeSelected: (LocalTime) -> Unit
    ): MaterialTimePicker {
        val timePickerBuilder =
            MaterialTimePicker.Builder()
                .setTimeFormat(if (is24HourFormat) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H)
                .setHour(selectedTime?.hour ?: 0)
                .setMinute(selectedTime?.minute ?: 0)
                .setTitleText(dialogTitle)

        return timePickerBuilder.build().also { picker ->
            picker.addOnPositiveButtonClickListener {
                onTimeSelected(LocalTime(picker.hour, picker.minute))
            }
        }
    }
}
