package com.icell.external.carlosformito.ui.theme

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import com.google.android.material.timepicker.TimeFormat
import java.time.format.DateTimeFormatter

/**
 * A class that provides default formatting options for date and time values used by the Carlos form fields.
 *
 * @property dateFormatter The [DateTimeFormatter] used for formatting dates.
 * Defaults to [DateTimeFormatter.ISO_LOCAL_DATE].
 * @property timeFormatter The [DateTimeFormatter] used for formatting times.
 * Defaults to "HH:mm" (24-hour format).
 * @param timeFormat An optional parameter to specify the time format, either 12-hour or 24-hour clock.
 * If null, the system's time format will be used.
 */
class CarlosFormatDefaults(
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE,
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm"),
    timeFormat: Int? = null
) {

    // Backing property to store the optional time format parameter
    private val timeFormatParam: Int? = timeFormat

    /**
     * The time format to be used, either provided or derived from the system settings.
     * This is a 12-hour or 24-hour clock format represented by [TimeFormat.CLOCK_12H] or [TimeFormat.CLOCK_24H].
     */
    val timeFormat: Int
        @ReadOnlyComposable
        @Composable
        get() = timeFormatParam ?: getSystemTimeFormat()

    /**
     * Retrieves the system's default time format (12-hour or 24-hour clock).
     *
     * @return [TimeFormat.CLOCK_24H] if the system is set to 24-hour format, [TimeFormat.CLOCK_12H] otherwise.
     */
    @ReadOnlyComposable
    @Composable
    private fun getSystemTimeFormat(): Int {
        val isSystem24Hour = DateFormat.is24HourFormat(LocalContext.current)
        return if (isSystem24Hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
    }
}

/**
 * A composition local provider for [CarlosFormatDefaults].
 * This allows for a consistent use of date and time formats across a composable hierarchy.
 */
val LocalCarlosFormats = staticCompositionLocalOf { CarlosFormatDefaults() }
