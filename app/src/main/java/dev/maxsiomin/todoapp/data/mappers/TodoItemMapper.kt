package dev.maxsiomin.todoapp.data.mappers

import dev.maxsiomin.todoapp.common.data.mappers.BidirectionalMapper
import dev.maxsiomin.todoapp.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.domain.model.TodoItem

class TodoItemMapper : BidirectionalMapper<TodoItemEntity, TodoItem> {

    override fun toDomain(data: TodoItemEntity): TodoItem {
        return TodoItem(
            id = data.id,
            description = data.description,
            priority = data.priority,
            progress = data.progress,
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
            progress = domain.progress,
            created = domain.created,
            modified = domain.modified,
            deadline = domain.modified,
        )
    }

}