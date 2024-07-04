package dev.maxsiomin.todoapp.feature.todolist.data.repository

import android.content.SharedPreferences
import androidx.room.withTransaction
import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.LocalError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.core.data.PrefsKeys
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.feature.todolist.data.local.DeletedItem
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase
import dev.maxsiomin.todoapp.feature.todolist.data.mappers.TodoItemMapper
import dev.maxsiomin.todoapp.feature.todolist.data.remote.TodoItemsApi
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.TodoItemDto
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import javax.inject.Inject

internal data class MergeRequest(
    val callback: suspend () -> Resource<Unit, DataError>,
    val deferred: CompletableDeferred<Resource<Unit, DataError>>
)

internal class TodoItemsRepositoryImpl @Inject constructor(
    private val db: TodoDatabase,
    private val api: TodoItemsApi,
    private val prefs: SharedPreferences,
    private val mapper: TodoItemMapper,
    private val dispatchers: DispatcherProvider,
) : TodoItemsRepository {

    private val mergeWithApiChannel =
        Channel<MergeRequest>().also { channel ->
            CoroutineScope(dispatchers.io).launch {
                launch {
                    processMergeRequests(channel)
                }
            }
        }

    private suspend fun processMergeRequests(
        mergeChannel: Channel<MergeRequest>
    ) {
        for (request in mergeChannel) {
            val result = request.callback()
            request.deferred.complete(result)
        }
    }

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

            val apiResponse = enqueueMergeWithApi()
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

        val revision = revision
        val apiResponse = api.addTodoItem(
            itemDto = mapper.fromDomainToDto(domain = item),
            revision = revision
        )
        when (apiResponse) {
            is Resource.Error -> Unit
            is Resource.Success -> {
                this@TodoItemsRepositoryImpl.revision = apiResponse.data.revision
            }
        }
    }

    override suspend fun editTodoItem(
        item: TodoItem
    ) = withContext(dispatchers.io) {

        db.todoDao.upsertTodoItem(item = mapper.fromDomainToEntity(item))

        val revision = revision
        val apiResponse = api.changeTodoItemById(
            itemDto = mapper.fromDomainToDto(item),
            revision = revision
        )
        when (apiResponse) {
            is Resource.Error -> Unit
            is Resource.Success -> {
                this@TodoItemsRepositoryImpl.revision = apiResponse.data.revision
            }
        }

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
        db.todoDao.addDeletedItem(DeletedItem(id = entity.id))

        val revision = revision
        val apiResponse = api.deleteTodoItem(
            id = item.id,
            revision = revision
        )
        when (apiResponse) {
            is Resource.Error -> Unit
            is Resource.Success -> {
                this@TodoItemsRepositoryImpl.revision = apiResponse.data.revision
            }
        }

        return@withContext
    }

    override suspend fun enqueueMergeWithApi(): Resource<Unit, DataError> {
        val deferred = CompletableDeferred<Resource<Unit, DataError>>()
        val request = MergeRequest(
            callback = { mergeWithApi() },
            deferred = deferred,
        )
        mergeWithApiChannel.send(request)
        return deferred.await()
    }

    private suspend fun mergeWithApi(): Resource<Unit, DataError> = withContext(dispatchers.io) {
        val itemsFromApi = when (val fetchResource = fetchFromApi()) {
            is Resource.Error -> {
                return@withContext Resource.Error(fetchResource.error)
            }
            is Resource.Success -> fetchResource.data
        }

        val localList = db.todoDao.getAllTodoItems().first().map { mapper.fromEntityToDomain(it) }
        val merged = mergedList(local = localList, remote = itemsFromApi).map { domain ->
            mapper.fromDomainToDto(domain)
        }

        val revision = when (val response = getRevision()) {
            is Resource.Error -> {
                return@withContext Resource.Error(response.error)
            }
            is Resource.Success -> response.data
        }
        val response = api.updateTodoItemsList(newList = merged, revision = revision)
        return@withContext when (response) {
            is Resource.Error -> {
                Resource.Error(response.error)
            }

            is Resource.Success -> {
                this@TodoItemsRepositoryImpl.revision = response.data.revision
                updateTodoItemsFromApi(response.data.items)
                Resource.Success(Unit)
            }
        }
    }

    private suspend fun mergedList(local: List<TodoItem>, remote: List<TodoItem>): List<TodoItem> {
        val merged = mutableListOf<TodoItem>()

        val deleted = db.todoDao.getDeletedItems().map { it.id }

        if (local.isEmpty() && remote.isEmpty()) {
            return merged
        }

        val together = local + remote
        for (item in together) {
            val currId = item.id
            if (currId in deleted || currId in merged.map { it.id }) {
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
