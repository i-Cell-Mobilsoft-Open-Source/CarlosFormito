package com.icell.external.carlosformito.ui.field.extension

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime

fun Long.toLocalDate(): LocalDate = ZonedDateTime
    .ofInstant(Instant.ofEpochMilli(this), ZoneId.systemDefault())
    .toLocalDate()

fun LocalDate.toEpochMillis(): Long = atStartOfDay()
    .atZone(ZoneId.systemDefault())
    .toInstant()
    .toEpochMilli()

fun LocalDate.isAfterOrEqual(localDate: LocalDate): Boolean =
    isAfter(localDate) || isEqual(localDate)

fun LocalDate.isBeforeOrEqual(localDate: LocalDate): Boolean =
    isBefore(localDate) || isEqual(localDate)

fun LocalDate.isBetween(minDate: LocalDate, maxDate: LocalDate): Boolean =
    isAfterOrEqual(minDate) && isBeforeOrEqual(maxDate)
