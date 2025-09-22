package hu.icellmobilsoft.carlosformito.ui.util

import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalTime

/**
 * Object that provides a builder function for creating and configuring a [MaterialTimePicker].
 */
object TimePickerBuilder {

    /**
     * Builds a [MaterialTimePicker] dialog with the specified configuration.
     *
     * @param dialogTitle The title to be displayed on the time picker dialog.
     * @param selectedTime The initial time to be displayed in the time picker.
     * If null, the time picker will default to 00:00. Defaults to null.
     * @param timeFormat The format in which time should be displayed, either 24-hour or 12-hour clock.
     * Defaults to 24-hour clock format ([TimeFormat.CLOCK_24H]).
     * @param onTimeSelected A callback that is invoked when the user selects a time.
     * The selected [LocalTime] is passed as an argument to this callback.
     * @return A configured instance of [MaterialTimePicker].
     */
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
