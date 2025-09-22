package hu.icellmobilsoft.carlosformito.ui.theme

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import java.time.format.DateTimeFormatter

/**
 * A class that provides default formatting options for date and time values used by the Carlos form fields.
 *
 * @property dateFormatter The [DateTimeFormatter] used for formatting dates. Defaults to ISO_LOCAL_DATE.
 * @property timeFormatter The [DateTimeFormatter] used for formatting times. Defaults to "HH:mm".
 * @property is24HourFormat indicates if the 24-hour format should be used. Defaults to true.
 */
class CarlosFormatDefaults(
    val dateFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE,
    val timeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm"),
    is24HourFormat: Boolean? = true
) {

    private val is24HourFormatParam: Boolean? = is24HourFormat

    /**
     * Gets whether the 24-hour - or the 12 hour (AM/ PM) - format should be used. Defaults to true.
     *
     * @return `true` if 24-hour format is used, `false` otherwise.
     */
    val is24HourFormat: Boolean
        @ReadOnlyComposable
        @Composable
        get() = is24HourFormatParam ?: DateFormat.is24HourFormat(LocalContext.current)
}

/**
 * A composition local provider for [CarlosFormatDefaults].
 * This allows for a consistent use of date and time formats across a composable hierarchy.
 */
val LocalCarlosFormats = staticCompositionLocalOf { CarlosFormatDefaults() }
