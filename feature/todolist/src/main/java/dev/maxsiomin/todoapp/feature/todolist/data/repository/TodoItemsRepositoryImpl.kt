package dev.maxsiomin.todoapp.feature.todolist.data.repository

import android.content.SharedPreferences
import androidx.room.withTransaction
import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.LocalError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.core.data.PrefsKeys
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase
import dev.maxsiomin.todoapp.feature.todolist.data.mappers.TodoItemMapper
import dev.maxsiomin.todoapp.feature.todolist.data.remote.TodoItemsApi
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.TodoItemDto
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class TodoItemsRepositoryImpl @Inject constructor(
    private val db: TodoDatabase,
    private val api: TodoItemsApi,
    private val prefs: SharedPreferences,
    private val mapper: TodoItemMapper,
    private val dispatchers: DispatcherProvider,
) : TodoItemsRepository {

    private var revision: Int = 0
        get() {
            return prefs.getInt(PrefsKeys.REVISION, 0)
        }
        set(value) {
            prefs.edit().putInt(PrefsKeys.REVISION, value).apply()
            field = value
        }

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

            val apiResponse = mergeWithApi()
            if (apiResponse is Resource.Error) {
                emit(Resource.Error(apiResponse.error))
            }

            dbFlow.collect {
                emit(Resource.Success(it))
            }
        }
    }

    override suspend fun addTodoItem(
        item: TodoItem
    ) = withContext(dispatchers.io) {

        db.todoDao.upsertTodoItem(item = mapper.fromDomainToEntity(item))

        mergeWithApi()

        return@withContext
    }

    override suspend fun editTodoItem(
        item: TodoItem
    ) = withContext(dispatchers.io) {

        db.todoDao.upsertTodoItem(item = mapper.fromDomainToEntity(item))

        mergeWithApi()

        return@withContext
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

    override suspend fun deleteTodoItem(
        item: TodoItem,
    ) = withContext(dispatchers.io) {

        val entity = mapper.fromDomainToEntity(item)
        db.todoDao.deleteTodoItem(entity)

        mergeWithApi()

        return@withContext
    }

    override suspend fun mergeWithApi(): Resource<Unit, DataError> {
        val itemsFromApi = when (val fetchResource = fetchFromApi()) {
            is Resource.Error -> return Resource.Error(fetchResource.error)
            is Resource.Success -> fetchResource.data
        }
        val currList = db.todoDao.getAllTodoItems().first().map { mapper.fromEntityToDomain(it) }

        val merged = mergedList(local = currList, remote = itemsFromApi).map { domain ->
            mapper.fromDomainToDto(domain)
        }

        val revision = when (val response = getRevision()) {
            is Resource.Error -> return Resource.Error(response.error)
            is Resource.Success -> response.data
        }
        val response = api.updateTodoItemsList(newList = merged, revision = revision)
        return when (response) {
            is Resource.Error -> {
                return Resource.Error(response.error)
            }

            is Resource.Success -> {
                this.revision = response.data.revision
                updateTodoItemsFromApi(response.data.items)
                Resource.Success(Unit)
            }
        }
    }

    private fun mergedList(local: List<TodoItem>, remote: List<TodoItem>): List<TodoItem> {
        val merged = mutableListOf<TodoItem>()

        if (local.isEmpty() && remote.isEmpty()) {
            return merged
        }

        val together = local + remote
        for (item in together) {
            val currId = item.id
            if (currId in merged.map { it.id }) {
                continue
            }
            val allWithCurrId = together.filter { it.id == currId }
            val neededItem = allWithCurrId.maxBy { it.modified }
            merged.add(neededItem)
        }

        return merged
    }

    private suspend fun getRevision(): Resource<Int, DataError> {
        return when (val response = api.getTodoItemsList()) {
            is Resource.Error -> Resource.Error(response.error)
            is Resource.Success -> Resource.Success(response.data.revision)
        }
    }

    private suspend fun fetchFromApi(): Resource<List<TodoItem>, DataError> {
        return when (val response = api.getTodoItemsList()) {
            is Resource.Error -> Resource.Error(response.error)
            is Resource.Success -> {
                revision = response.data.revision
                Resource.Success(response.data.items.map { mapper.fromDtoToDomain(it) })
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

}