package com.icell.external.carlosformito.ui.theme

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.time.format.DateTimeFormatter

class CarlosFormatDefaults(
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE,
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm"),
    is24HourFormat: Boolean? = true
) {

    private val is24HourFormatParam: Boolean? = is24HourFormat

    val is24HourFormat: Boolean
        @ReadOnlyComposable
        @Composable
        get() = is24HourFormatParam ?: DateFormat.is24HourFormat(LocalContext.current)
}

val LocalCarlosFormats = staticCompositionLocalOf { CarlosFormatDefaults() }
