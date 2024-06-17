package dev.maxsiomin.todoapp.data.local.converters

import androidx.room.TypeConverter
import dev.maxsiomin.todoapp.domain.model.Progress

class ProgressConverters {

    private fun buildProgressMap(): Map<String, Progress> {
        val progressesList = listOf(
            Progress.NotCompleted,
            Progress.Completed
        )

        return buildMap {
            for (progress in progressesList) {
                put(progress.toString(), progress)
            }
        }
    }

    private val progressMap = buildProgressMap()

    @TypeConverter
    fun fromProgressToString(progress: Progress): String {
        return progress.toString()
    }

    @TypeConverter
    fun fromStringToProgress(value: String): Progress {
        return progressMap[value]!!
    }

}