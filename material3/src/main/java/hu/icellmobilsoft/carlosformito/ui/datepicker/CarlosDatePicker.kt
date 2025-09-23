package hu.icellmobilsoft.carlosformito.ui.datepicker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hu.icellmobilsoft.carlosformito.ui.extension.selectedDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlin.time.ExperimentalTime

/**
 * A composable function that displays a date picker dialog.
 *
 * This function shows a [DatePickerDialog] with the provided title, optional headline, and selectable date range.
 * The selected date is formatted using the provided [format] and passed to the
 * [onSelectDate] callback upon confirmation. The dialog can be dismissed using the [hideDialog] callback.
 *
 * @param dialogTitle The title of the date picker dialog.
 * @param dialogHeadline An optional headline to display above the date picker.
 * @param selectedDate The initially selected date. If `null`, no date is initially selected.
 * @param minDate The minimum selectable date. If null, defaults to [CarlosDatePickerDefaults.ABSOLUTE_MIN_DATE].
 * @param maxDate The maximum selectable date. If `null`, defaults to [CarlosDatePickerDefaults.ABSOLUTE_MAX_DATE].
 * @param format The [DateTimeFormat] used to format the selected date for display.
 * @param onSelectDate Lambda function to be invoked when a date is selected. Receives the selected date as a parameter.
 * @param hideDialog Lambda function to be invoked to dismiss the dialog.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun CarlosDatePicker(
    dialogTitle: String,
    dialogHeadline: String? = null,
    selectedDate: LocalDate?,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    format: DateTimeFormat<LocalDate>,
    onSelectDate: (LocalDate?) -> Unit,
    hideDialog: () -> Unit,
    confirmButtonText: String,
    dismissButtonText: String
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.atStartOfDayIn(TimeZone.UTC)?.toEpochMilliseconds(),
        selectableDates = CarlosDatePickerDefaults.selectableDates(minDate, maxDate),
        initialDisplayMode = DisplayMode.Picker
    )
    val confirmEnabled by remember {
        derivedStateOf { datePickerState.selectedDateMillis != null }
    }

    DatePickerDialog(
        onDismissRequest = {
            hideDialog.invoke()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSelectDate.invoke(datePickerState.selectedDate)
                },
                enabled = confirmEnabled
            ) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    hideDialog.invoke()
                }
            ) {
                Text(text = dismissButtonText)
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = dialogTitle,
                    modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 24.dp)
                )
            },
            headline = {
                Text(
                    text = datePickerState.selectedDate?.format(format) ?: dialogHeadline ?: "",
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
        )
    }
}
