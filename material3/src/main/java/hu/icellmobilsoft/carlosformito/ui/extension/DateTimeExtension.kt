package hu.icellmobilsoft.carlosformito.ui.extension

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * Extension function to convert epoch milliseconds to [LocalDate] in a specified time zone.
 */
@OptIn(ExperimentalTime::class)
internal fun Long.toLocalDate(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date
