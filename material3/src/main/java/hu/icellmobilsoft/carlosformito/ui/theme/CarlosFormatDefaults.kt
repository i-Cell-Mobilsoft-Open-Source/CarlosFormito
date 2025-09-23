package hu.icellmobilsoft.carlosformito.ui.theme

import android.text.format.DateFormat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat

/**
 * Provides default formatting options for date and time values used by Carlos form fields.
 *
 * @property dateFormat The [DateTimeFormat] used for formatting dates.
 * Defaults to [LocalDate.Formats.ISO].
 * @property timeFormat The [DateTimeFormat] used for formatting times.
 * Defaults to [LocalTime.Formats.ISO].
 * @param is24HourFormat Optional. Specifies whether to use a 12-hour or 24-hour clock.
 * If `null`, the system’s current time format will be used.
 */
class CarlosFormatDefaults(
    val dateFormat: DateTimeFormat<LocalDate> = LocalDate.Formats.ISO,
    val timeFormat: DateTimeFormat<LocalTime> = LocalTime.Formats.ISO,
    is24HourFormat: Boolean? = null
) {

    // Backing property for the optional time format parameter
    private val is24HourFormatParam: Boolean? = is24HourFormat

    /**
     * Whether the 24-hour clock format should be used.
     *
     * If [is24HourFormatParam] is provided, it is returned directly.
     * Otherwise, the value is derived from the system settings.
     *
     * @return `true` for 24-hour format, `false` for 12-hour format.
     */
    val is24HourFormat: Boolean
        @ReadOnlyComposable
        @Composable
        get() = is24HourFormatParam ?: getSystemTimeFormat()

    /**
     * Retrieves the system’s default time format.
     *
     * @return `true` if the system is set to 24-hour format, `false` otherwise.
     */
    @ReadOnlyComposable
    @Composable
    private fun getSystemTimeFormat(): Boolean = DateFormat.is24HourFormat(LocalContext.current)
}

/**
 * A [staticCompositionLocalOf] provider for [CarlosFormatDefaults].
 *
 * This enables consistent date and time formatting across a Compose hierarchy
 * by providing [CarlosFormatDefaults] at the composition level.
 */
val LocalCarlosFormats = staticCompositionLocalOf { CarlosFormatDefaults() }
