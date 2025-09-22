package hu.icellmobilsoft.carlosformito.ui.extension

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.LocalDate
import java.time.LocalTime

/**
 * Extension property for [DatePickerState] that retrieves the selected date as a [LocalDate].
 *
 * This property converts the selected date in milliseconds to a [LocalDate] using the
 * `toLocalDate()` extension function.
 *
 * @return The selected date as a [LocalDate], or `null` if no date is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
val DatePickerState.selectedDate: LocalDate?
    get() = selectedDateMillis?.toLocalDate()

/**
 * Extension property for [TimePickerState] that retrieves the selected time as a [LocalTime].
 *
 * This property creates a [LocalTime] object from the selected hour and minute values.
 *
 * @return The selected time as a [LocalTime].
 */
@OptIn(ExperimentalMaterial3Api::class)
val TimePickerState.selectedTime: LocalTime?
    get() = LocalTime.of(hour, minute)
