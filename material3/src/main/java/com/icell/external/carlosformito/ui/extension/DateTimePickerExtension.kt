package com.icell.external.carlosformito.ui.extension

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
val DatePickerState.selectedDate: LocalDate?
    get() = selectedDateMillis?.toLocalDate()

@OptIn(ExperimentalMaterial3Api::class)
val TimePickerState.selectedTime: LocalTime?
    get() = LocalTime.of(hour, minute)
