package dev.maxsiomin.todoapp.feature.todolist.data.mappers

import dev.maxsiomin.common.data.mappers.BidirectionalMapper
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import javax.inject.Inject

internal class TodoItemMapper @Inject constructor() : BidirectionalMapper<TodoItemEntity, TodoItem> {

    override fun toDomain(data: TodoItemEntity): TodoItem {
        return TodoItem(
            id = data.id,
            description = data.description,
            priority = data.priority,
            isCompleted = data.isCompleted,
            created = data.created,
            modified = data.modified,
            deadline = data.deadline
        )
    }

    override fun toData(domain: TodoItem): TodoItemEntity {
        return TodoItemEntity(
            id = domain.id,
            description = domain.description,
            priority = domain.priority,
            isCompleted = domain.isCompleted,
            created = domain.created,
            modified = domain.modified,
            deadline = domain.deadline,
        )
    }

}