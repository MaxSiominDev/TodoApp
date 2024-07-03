package dev.maxsiomin.todoapp.feature.todolist.data.mappers

import dev.maxsiomin.common.extensions.toEpochMillis
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
            deadline = entity.deadline,
            lastUpdatedBy = entity.lastUpdatedBy,
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
            lastUpdatedBy = domain.lastUpdatedBy,
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
            created = dto.createdAt,
            modified = dto.changedAt,
            deadline = dto.deadline?.toLocalDate(),
            lastUpdatedBy = dto.lastUpdatedBy,
        )
    }

    fun fromDomainToDto(domain: TodoItem): TodoItemDto {
        val entity = this.fromDomainToEntity(domain)
        return this.fromEntityToDto(entity)
    }

    fun fromDtoToDomain(dto: TodoItemDto): TodoItem {
        val entity = this.fromDtoToEntity(dto)
        return this.fromEntityToDomain(entity)
    }

    fun fromEntityToDto(entity: TodoItemEntity): TodoItemDto {
        val importance = when (entity.priority) {
            Priority.Default -> "basic"
            Priority.High -> "high"
            Priority.Low -> "low"
        }
        return TodoItemDto(
            id = entity.id,
            description = entity.description,
            importance = importance,
            deadline = entity.deadline?.toEpochMillis(),
            isCompleted = entity.isCompleted,
            color = null,
            createdAt = entity.created,
            changedAt = entity.modified,
            lastUpdatedBy = entity.lastUpdatedBy,
        )
    }

}