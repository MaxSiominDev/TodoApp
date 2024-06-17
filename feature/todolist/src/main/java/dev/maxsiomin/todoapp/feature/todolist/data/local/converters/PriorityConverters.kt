package dev.maxsiomin.todoapp.feature.todolist.data.local.converters

import androidx.room.TypeConverter
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority

internal class PriorityConverters {

    private fun buildPrioritiesMap(): Map<String, Priority> {
        val prioritiesList = listOf(
            Priority.Low,
            Priority.Default,
            Priority.High
        )

        return buildMap {
            for (priority in prioritiesList) {
                put(priority.toString(), priority)
            }
        }
    }

    private val prioritiesMap = buildPrioritiesMap()

    @TypeConverter
    fun fromPriorityToString(priority: Priority): String {
        return priority.toString()
    }

    @TypeConverter
    fun fromStringToPriority(value: String): Priority {
        return prioritiesMap[value]!!
    }

}