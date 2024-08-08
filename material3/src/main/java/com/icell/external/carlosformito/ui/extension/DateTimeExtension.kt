package com.icell.external.carlosformito.ui.extension

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Extension function to convert epoch milliseconds to [LocalDate] in a specified time zone.
 */
fun Long.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate = ZonedDateTime
    .ofInstant(Instant.ofEpochMilli(this), zoneId)
    .toLocalDate()

/**
 * Extension function to convert a [LocalDate] to epoch milliseconds in a specified time zone.
 *
 * This function represents the start of the day in the given time zone and converts it to milliseconds since the epoch.
 *
 * @param zoneId The time zone to use for the conversion. Defaults to the system default time zone.
 * @return The number of milliseconds since the epoch for the start of the day in the specified time zone.
 */
fun LocalDate.toEpochMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long = atStartOfDay()
    .atZone(zoneId)
    .toInstant()
    .toEpochMilli()

/**
 * Extension function to check if the current [LocalDate] is after or equal to another [LocalDate].
 *
 * @param localDate The [LocalDate] to compare against.
 * @return `true` if the current [LocalDate] is after or equal to the specified [LocalDate], otherwise `false`.
 */
fun LocalDate.isAfterOrEqual(localDate: LocalDate): Boolean =
    isAfter(localDate) || isEqual(localDate)

/**
 * Extension function to check if the current [LocalDate] is before or equal to another [LocalDate].
 *
 * @param localDate The [LocalDate] to compare against.
 * @return `true` if the current [LocalDate] is before or equal to the specified [LocalDate], otherwise `false`.
 */
fun LocalDate.isBeforeOrEqual(localDate: LocalDate): Boolean =
    isBefore(localDate) || isEqual(localDate)

/**
 * Extension function to check if the current [LocalDate] is between two other [LocalDate] values, inclusive.
 *
 * @param minDate The minimum [LocalDate] in the range (inclusive).
 * @param maxDate The maximum [LocalDate] in the range (inclusive).
 * @return `true` if the current [LocalDate] is between [minDate] and [maxDate], inclusive, otherwise `false`.
 */
fun LocalDate.isBetween(minDate: LocalDate, maxDate: LocalDate): Boolean =
    isAfterOrEqual(minDate) && isBeforeOrEqual(maxDate)
