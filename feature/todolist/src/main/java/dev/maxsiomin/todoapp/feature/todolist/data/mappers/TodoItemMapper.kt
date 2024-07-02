package dev.maxsiomin.todoapp.feature.todolist.data.mappers

import dev.maxsiomin.common.extensions.toLocalDate
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.TodoItemDto
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import javax.inject.Inject

internal class TodoItemMapper @Inject constructor() {

    fun fromEntityToDomain(entity: TodoItemEntity): TodoItem {
        return TodoItem(
            id = entity.id,
            description = entity.description,
            priority = entity.priority,
            isCompleted = entity.isCompleted,
            created = entity.created,
            modified = entity.modified,
            deadline = entity.deadline
        )
    }

    fun fromDomainToEntity(domain: TodoItem): TodoItemEntity {
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

    fun fromDtoToEntity(dto: TodoItemDto): TodoItemEntity {
        val priority = when (dto.importance) {
            "low" -> Priority.Low
            "basic" -> Priority.Default
            "high" -> Priority.High
            // Else branch would be called only if undocumented behaviour of backend
            else -> Priority.Default
        }
        return TodoItemEntity(
            id = dto.id,
            description = dto.description,
            priority = priority,
            isCompleted = dto.isCompleted,
            created = dto.createdAt.toLocalDate(),
            modified = dto.changedAt.toLocalDate(),
            deadline = dto.deadline?.toLocalDate(),
        )
    }

}