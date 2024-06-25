package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import dev.maxsiomin.common.extensions.toLocalizedDate
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Progress
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem

internal data class TodoItemUiModel(
    val id: String,
    val description: String,
    val priority: Priority,
    val progress: Progress,
    val deadline: String? = null,
)

internal fun TodoItem.toTodoItemUiModel(): TodoItemUiModel {
    val deadline = this.deadline?.toLocalizedDate()
    return TodoItemUiModel(
        id = this.id,
        description = this.description,
        deadline = deadline,
        progress = this.progress,
        priority = this.priority
    )
}
