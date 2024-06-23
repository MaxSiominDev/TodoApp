package dev.maxsiomin.todoapp.feature.todolist.data.local.converters

import androidx.room.TypeConverter
import dev.maxsiomin.common.extensions.toEpochMillis
import dev.maxsiomin.common.extensions.toLocalDate
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime

internal class LocalDateConverters {

    @TypeConverter
    fun fromLocalDateToLong(localDate: LocalDate): Long = localDate.toEpochMillis()

    @TypeConverter
    fun fromLongToLocalDate(value: Long): LocalDate = value.toLocalDate()

}