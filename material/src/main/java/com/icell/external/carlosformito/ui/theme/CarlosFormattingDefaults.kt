package com.icell.external.carlosformito.ui.theme

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.timepicker.TimeFormat
import java.time.format.DateTimeFormatter

class CarlosFormattingDefaults(
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE,
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm"),
    timeFormat: Int? = null
) {

    private val timeFormatParam: Int? = timeFormat

    val timeFormat: Int
        @ReadOnlyComposable
        @Composable
        get() = timeFormatParam ?: getSystemTimeFormat()

    @ReadOnlyComposable
    @Composable
    private fun getSystemTimeFormat(): Int {
        val isSystem24Hour = DateFormat.is24HourFormat(LocalContext.current)
        return if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
    }
}

val LocalCarlosFormats = staticCompositionLocalOf { CarlosFormattingDefaults() }
