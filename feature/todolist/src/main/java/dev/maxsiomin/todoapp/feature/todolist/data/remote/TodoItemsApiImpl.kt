package dev.maxsiomin.todoapp.feature.todolist.data.remote

import dev.maxsiomin.common.domain.resource.NetworkError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.common.util.isDebug
import dev.maxsiomin.todoapp.core.data.safeGet
import dev.maxsiomin.todoapp.core.util.ApiKeys
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.AddTodoItemResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.ChangeTodoItemByIdResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.DeleteTodoItemResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.GetTodoItemByIdResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.GetTodoItemsListResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.TodoItemDto
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.url
import javax.inject.Inject

class TodoItemsApiImpl @Inject constructor(
    private val httpClient: HttpClient,
) : TodoItemsApi {

    override suspend fun getTodoItemsList(): Resource<GetTodoItemsListResponse, NetworkError> {
        return httpClient.safeGet<GetTodoItemsListResponse> {
            url(HttpRoutes.getTodoItemsListRoute())
            header("Authorization", "Bearer ${ApiKeys.YANDEX_API_KEY}")
            header("X-Generate-Fails", FAILS_THRESHOLD)
        }
    }

    override suspend fun updateTodoItemsList(newList: List<TodoItemDto>): Resource<GetTodoItemsListResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun getTodoItemById(id: String): Resource<GetTodoItemByIdResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun addTodoItem(itemDto: TodoItemDto): Resource<AddTodoItemResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun changeTodoItemById(itemDto: TodoItemDto): Resource<ChangeTodoItemByIdResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTodoItem(id: String): Resource<DeleteTodoItemResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DEBUG_FAILS_THRESHOLD = 70
        private const val RELEASE_FAILS_THRESHOLD = 0
        private val FAILS_THRESHOLD =
            (if (isDebug()) DEBUG_FAILS_THRESHOLD else RELEASE_FAILS_THRESHOLD).toString()
    }

}