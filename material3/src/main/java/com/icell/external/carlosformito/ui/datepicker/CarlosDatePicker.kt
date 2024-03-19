package com.icell.external.carlosformito.ui.datepicker

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.icell.external.carlosformito.ui.extension.selectedDate
import com.icell.external.carlosformito.ui.extension.toEpochMillis
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val ABSOLUTE_MIN_DATE: LocalDate = LocalDate.of(1900, 1, 1)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarlosDatePicker(
    dialogTitle: String,
    dialogHeadline: String? = null,
    selectedDate: LocalDate?,
    minDate: LocalDate? = null,
    maxDate: LocalDate? = null,
    formatter: DateTimeFormatter,
    onSelectDate: (LocalDate?) -> Unit,
    hideDialog: () -> Unit
) {
    val selectableDates = rememberSelectableDates(minDate ?: ABSOLUTE_MIN_DATE, maxDate)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.toEpochMillis(ZoneOffset.UTC),
        selectableDates = selectableDates,
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
                Text(
                    text = stringResource(id = android.R.string.ok)
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    hideDialog.invoke()
                }
            ) {
                Text(
                    text = stringResource(id = android.R.string.cancel)
                )
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
                    text = datePickerState.selectedDate?.format(formatter) ?: dialogHeadline ?: "",
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberSelectableDates(
    minDate: LocalDate?,
    maxDate: LocalDate?
): SelectableDates {
    return remember(minDate, maxDate) {
        object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val minEpochMilli = minDate?.toEpochMillis(ZoneOffset.UTC)
                val maxEpochMilli = maxDate?.toEpochMillis(ZoneOffset.UTC)
                return when {
                    minEpochMilli != null && maxEpochMilli != null ->
                        utcTimeMillis >= minEpochMilli && utcTimeMillis <= maxEpochMilli

                    minEpochMilli != null -> utcTimeMillis >= minEpochMilli
                    maxEpochMilli != null -> utcTimeMillis <= maxEpochMilli
                    else -> false
                }
            }
        }
    }
}
