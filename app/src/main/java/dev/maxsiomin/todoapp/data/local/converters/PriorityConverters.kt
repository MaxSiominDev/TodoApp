package dev.maxsiomin.todoapp.data.local.converters

import androidx.room.TypeConverter
import dev.maxsiomin.todoapp.domain.model.Priority

class PriorityConverters {

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