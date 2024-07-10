package dev.maxsiomin.common.extensions

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun LocalDate.Companion.now(timeZone: TimeZone = TimeZone.UTC): LocalDate =
    Clock.System.now().toLocalDateTime(timeZone).date

fun LocalDate.toEpochMillis(timeZone: TimeZone = TimeZone.UTC): Long =
    this.atStartOfDayIn(timeZone).toEpochMilliseconds()

fun Long.toLocalDate(timeZone: TimeZone = TimeZone.UTC): LocalDate =
    Instant.fromEpochMilliseconds(this).toLocalDateTime(timeZone).date

fun LocalDate.toLocalizedDate(formatStyle: FormatStyle = FormatStyle.LONG): String {
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(formatStyle)
    return this.toJavaLocalDate().format(dateFormatter)
}