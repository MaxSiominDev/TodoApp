package dev.maxsiomin.todoapp.feature.todolist.domain.model

import kotlinx.datetime.LocalDate

internal data class TodoItem(
    val id: String,
    val description: String,
    val priority: Priority,
    val progress: Progress,
    val created: LocalDate,
    val modified: LocalDate? = null,
    val deadline: LocalDate? = null,
)

internal sealed class Priority {
    data object Low : Priority()
    data object Default : Priority()
    data object High : Priority()
}

/**
 * Of course, I could use boolean instead, but in case I'd like to do some other progress,
 * for example, completion, then it will cause difficult refactoring, and with this convenient class
 * I can add whatever status I want very easily
 */
internal sealed class Progress {
    data object NotCompleted : Progress()
    data object Completed : Progress()
}