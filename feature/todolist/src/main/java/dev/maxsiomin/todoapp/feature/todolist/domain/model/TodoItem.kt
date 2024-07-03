package dev.maxsiomin.todoapp.feature.todolist.domain.model

import kotlinx.datetime.LocalDate

internal data class TodoItem(
    val id: String,
    val description: String,
    val priority: Priority,
    val isCompleted: Boolean,
    val created: Long,
    val modified: Long,
    val deadline: LocalDate? = null,
    val lastUpdatedBy: String,
)

internal sealed class Priority {
    data object Low : Priority()
    data object Default : Priority()
    data object High : Priority()
}
