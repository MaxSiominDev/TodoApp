package dev.maxsiomin.todoapp.feature.todolist.data.remote

import dev.maxsiomin.common.domain.resource.NetworkError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.common.util.isDebug
import dev.maxsiomin.todoapp.core.data.ktor.safeGet
import dev.maxsiomin.todoapp.core.data.ktor.safePost
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.AddTodoItemRequest
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.AddTodoItemResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.ChangeTodoItemByIdResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.DeleteTodoItemResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.GetTodoItemByIdResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.GetTodoItemsListResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.TodoItemDto
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.UpdateTodoItemsListResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import javax.inject.Inject

class TodoItemsApiImpl @Inject constructor(
    private val httpClient: HttpClient,
) : TodoItemsApi {

    override suspend fun getTodoItemsList(): Resource<GetTodoItemsListResponse, NetworkError> {
        return httpClient.safeGet<GetTodoItemsListResponse> {
            url(HttpRoutes.getTodoItemsListRoute())
            header(Headers.GENERATE_FAILS, FAILS_THRESHOLD)
        }
    }

    override suspend fun updateTodoItemsList(
        newList: List<TodoItemDto>,
        revision: Int
    ): Resource<UpdateTodoItemsListResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun getTodoItemById(id: String): Resource<GetTodoItemByIdResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun addTodoItem(
        itemDto: TodoItemDto,
        revision: Int
    ): Resource<AddTodoItemResponse, NetworkError> {
        val body = AddTodoItemRequest(
            status = "ok",
            item = itemDto,
            revision = revision,
        )
        return httpClient.safePost<AddTodoItemResponse> {
            url(HttpRoutes.getTodoItemsListRoute())
            setBody(body)
            header(Headers.GENERATE_FAILS, FAILS_THRESHOLD)
            header(Headers.LAST_KNOWN_REVISION, revision.toString())
        }
    }

    override suspend fun changeTodoItemById(itemDto: TodoItemDto): Resource<ChangeTodoItemByIdResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTodoItem(id: String): Resource<DeleteTodoItemResponse, NetworkError> {
        TODO("Not yet implemented")
    }

    companion object {
        private const val DEBUG_FAILS_THRESHOLD = 100
        private const val RELEASE_FAILS_THRESHOLD = 0
        private val FAILS_THRESHOLD =
            (if (isDebug()) DEBUG_FAILS_THRESHOLD else RELEASE_FAILS_THRESHOLD).toString()
    }

    private object Headers {
        const val GENERATE_FAILS = "X-Generate-Fails"
        const val LAST_KNOWN_REVISION = "X-Last-Known-Revision"
    }

}