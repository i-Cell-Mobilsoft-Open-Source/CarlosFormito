package hu.icellmobilsoft.carlosformito.ui.util

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.CompositeDateValidator
import com.google.android.material.datepicker.DateValidatorPointBackward
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Object that provides a builder function for creating and configuring a [MaterialDatePicker].
 */
object DatePickerBuilder {

    /**
     * The absolute minimum date that can be selected, set to January 1, 1900.
     * */
    private val ABSOLUTE_MIN_DATE: LocalDate = LocalDate(1900, 1, 1)

    /**
     * Builds a [MaterialDatePicker] dialog with the specified configuration.
     *
     * @param dialogTitle The title to be displayed on the date picker dialog.
     * @param minDate The minimum selectable date in the date picker. If null, defaults to [absoluteMinDate].
     * @param maxDate The maximum selectable date in the date picker. If null, there is no upper limit.
     * @param selectedDate The initial date to be selected in the date picker. If null, no date is preselected.
     * @param onDateSelected A callback that is invoked when the user selects a date.
     * The selected [LocalDate] is passed as an argument to this callback.
     * @param absoluteMinDate The absolute minimum date that can be selected, defaults to [ABSOLUTE_MIN_DATE].
     * @return A configured instance of [MaterialDatePicker].
     */
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

    /**
     * Converts a [LocalDate] to UTC milliseconds since the epoch.
     *
     * @param localDate The [LocalDate] to be converted.
     * @return The UTC milliseconds since the epoch corresponding to the start of the day in UTC.
     */
    @OptIn(ExperimentalTime::class)
    private fun localDateToUtcMillis(localDate: LocalDate): Long {
        return localDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }

    /**
     * Converts UTC milliseconds since the epoch to a [LocalDate].
     *
     * @param utcMillis The UTC milliseconds since the epoch.
     * @return The corresponding [LocalDate].
     */
    @OptIn(ExperimentalTime::class)
    private fun localDateFromUtcMillis(utcMillis: Long): LocalDate {
        return Instant.fromEpochMilliseconds(utcMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date
    }
}
