package dev.maxsiomin.common.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.UTC) =
    Clock.System.now().toLocalDateTime(timeZone).date

fun LocalDate.toEpochMillis(timeZone: TimeZone = TimeZone.UTC): Long =
    this.atStartOfDayIn(timeZone).toEpochMilliseconds()

fun Long.toLocalDate(timeZone: TimeZone = TimeZone.UTC): LocalDate =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date