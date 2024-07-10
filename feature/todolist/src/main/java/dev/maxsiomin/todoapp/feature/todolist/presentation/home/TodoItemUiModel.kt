package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import dev.maxsiomin.common.extensions.toLocalizedDate
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem

/** Represents a todo item for UI */
internal data class TodoItemUiModel(
    val id: String,
    val description: String,
    val priority: Priority,
    val isCompleted: Boolean,
    val deadline: String? = null,
)

internal fun TodoItem.toTodoItemUiModel(): TodoItemUiModel {
    val deadline = this.deadline?.toLocalizedDate()
    return TodoItemUiModel(
        id = this.id,
        description = this.description,
        deadline = deadline,
        isCompleted = isCompleted,
        priority = this.priority
    )
}
