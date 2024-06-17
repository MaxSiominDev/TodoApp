package dev.maxsiomin.todoapp.feature.todolist.data.repository

import dev.maxsiomin.common.data.mappers.BidirectionalMapper
import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDao
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class TodoItemsRepositoryImpl @Inject constructor(
    private val todoDao: TodoDao,
    private val mapper: BidirectionalMapper<TodoItemEntity, TodoItem>,
): TodoItemsRepository {

    override fun getAllTodoItems(): Flow<Resource<List<TodoItem>, DataError>> {
        return todoDao.getAllTodoItems().map { entities ->
            val domainModels = entities.map { entity ->
                mapper.toDomain(entity)
            }
            Resource.Success(domainModels)
        }
    }

    override suspend fun addTodoItem(item: TodoItem) {
        val entity = mapper.toData(item)
        todoDao.upsertTodoItem(entity)
    }

}