package dev.maxsiomin.todoapp.feature.todolist.data.local.converters

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

internal class LocalDateConverters {

    @TypeConverter
    fun fromLocalDateToLong(localDate: LocalDate): Long {
        val startOfDay = localDate.atStartOfDayIn(TimeZone.UTC)
        return startOfDay.toEpochMilliseconds()
    }

    @TypeConverter
    fun fromLongToLocalDate(value: Long): LocalDate {
        val startOfDay = Instant.fromEpochMilliseconds(value)
        return startOfDay.toLocalDateTime(TimeZone.UTC).date
    }

}