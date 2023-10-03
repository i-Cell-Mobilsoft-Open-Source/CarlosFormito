package com.icell.external.carlosformito.ui.extension

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun Long.toLocalDate(zoneId: ZoneId = ZoneId.systemDefault()): LocalDate = ZonedDateTime
    .ofInstant(Instant.ofEpochMilli(this), zoneId)
    .toLocalDate()

fun LocalDate.toEpochMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long = atStartOfDay()
    .atZone(zoneId)
    .toInstant()
    .toEpochMilli()

fun LocalDate.isAfterOrEqual(localDate: LocalDate): Boolean =
    isAfter(localDate) || isEqual(localDate)

fun LocalDate.isBeforeOrEqual(localDate: LocalDate): Boolean =
    isBefore(localDate) || isEqual(localDate)

fun LocalDate.isBetween(minDate: LocalDate, maxDate: LocalDate): Boolean =
    isAfterOrEqual(minDate) && isBeforeOrEqual(maxDate)
