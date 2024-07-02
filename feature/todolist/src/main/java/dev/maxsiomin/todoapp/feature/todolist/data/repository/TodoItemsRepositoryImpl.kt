package dev.maxsiomin.todoapp.feature.todolist.data.repository

import androidx.room.withTransaction
import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.LocalError
import dev.maxsiomin.common.domain.resource.NetworkError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.feature.todolist.data.mappers.TodoItemMapper
import dev.maxsiomin.todoapp.feature.todolist.data.remote.TodoItemsApi
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.TodoItemDto
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TodoItemsRepositoryImpl @Inject constructor(
    private val db: TodoDatabase,
    private val api: TodoItemsApi,
    private val mapper: TodoItemMapper,
    private val dispatchers: DispatcherProvider,
) : TodoItemsRepository {

    private var revision: Int? = null

    override fun getAllTodoItems(): Flow<Resource<List<TodoItem>, DataError>> {
        return flow {
            val dbFlow = db.todoDao.getAllTodoItems().map { entities ->
                val domainModels = entities.map { entity ->
                    mapper.fromEntityToDomain(entity)
                }
                domainModels
            }

            val initialData = dbFlow.first()
            emit(Resource.Success(initialData))

            val response = api.getTodoItemsList()
            when (response) {
                is Resource.Error -> {
                    emit(Resource.Error(response.error))
                }

                is Resource.Success -> {
                    revision = response.data.revision
                    updateTodoItemsFromApi(response.data.items)
                }
            }

            dbFlow.collect {
                emit(Resource.Success(it))
            }
        }
    }

    private suspend fun updateTodoItemsFromApi(
        newItems: List<TodoItemDto>
    ) = withContext(dispatchers.io) {
        val entities = newItems.map { dto ->
            mapper.fromDtoToEntity(dto)
        }
        db.withTransaction {
            db.todoDao.clear()
            db.todoDao.insertAll(entities)
        }
    }

    override suspend fun addTodoItem(
        item: TodoItem
    ): Resource<Unit, DataError> = withContext(dispatchers.io) {

        db.todoDao.upsertTodoItem(item = mapper.fromDomainToEntity(item))

        val revision = revision ?: run {
            when (val revisionResponse = api.getTodoItemsList()) {
                is Resource.Error -> return@withContext Resource.Error(revisionResponse.error)
                is Resource.Success -> revisionResponse.data.revision.also { revision = it }
            }
        }
        val apiResponse = api.addTodoItem(
            itemDto = mapper.fromDomainToDto(domain = item),
            revision = revision
        )
        when (apiResponse) {
            is Resource.Error -> {
                Resource.Error(apiResponse.error)
            }
            is Resource.Success -> {
                this@TodoItemsRepositoryImpl.revision = apiResponse.data.revision
                Resource.Success(Unit)
            }
        }
    }

    override suspend fun getTodoItemById(
        id: String
    ): Resource<TodoItem, DataError> = withContext(dispatchers.io) {
        val item = db.todoDao.getTodoItemById(id)?.let { entity ->
            mapper.fromEntityToDomain(entity)
        }
        return@withContext if (item == null) {
            Resource.Error(LocalError.NotFound)
        } else {
            Resource.Success(item)
        }
    }

    override suspend fun deleteTodoItem(item: TodoItem) = withContext(dispatchers.io) {
        val entity = mapper.fromDomainToEntity(item)
        db.todoDao.deleteTodoItem(entity)
    }

}