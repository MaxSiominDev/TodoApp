package dev.maxsiomin.todoapp.data.repository

import dev.maxsiomin.todoapp.common.data.mappers.BidirectionalMapper
import dev.maxsiomin.todoapp.common.domain.resource.DataError
import dev.maxsiomin.todoapp.common.domain.resource.Resource
import dev.maxsiomin.todoapp.data.local.TodoDao
import dev.maxsiomin.todoapp.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.domain.model.TodoItem
import dev.maxsiomin.todoapp.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoItemsRepositoryImpl @Inject constructor(
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