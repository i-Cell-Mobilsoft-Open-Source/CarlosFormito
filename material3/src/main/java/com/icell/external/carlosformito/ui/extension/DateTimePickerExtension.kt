package com.icell.external.carlosformito.ui.extension

import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
val DatePickerState.selectedDate: LocalDate?
    get() = selectedDateMillis?.toLocalDate()
