package dev.maxsiomin.todoapp.feature.todolist.data.remote

import dev.maxsiomin.common.domain.resource.NetworkError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.AddTodoItemResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.ChangeTodoItemByIdResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.DeleteTodoItemResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.GetTodoItemByIdResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.GetTodoItemsListResponse
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.TodoItemDto
import dev.maxsiomin.todoapp.feature.todolist.data.remote.dto.UpdateTodoItemsListResponse

interface TodoItemsApi {

    suspend fun getTodoItemsList(): Resource<GetTodoItemsListResponse, NetworkError>

    suspend fun updateTodoItemsList(
        newList: List<TodoItemDto>,
        revision: Int
    ): Resource<UpdateTodoItemsListResponse, NetworkError>

    suspend fun getTodoItemById(id: String): Resource<GetTodoItemByIdResponse, NetworkError>

    suspend fun addTodoItem(
        itemDto: TodoItemDto,
        revision: Int
    ): Resource<AddTodoItemResponse, NetworkError>

    suspend fun changeTodoItemById(itemDto: TodoItemDto): Resource<ChangeTodoItemByIdResponse, NetworkError>

    suspend fun deleteTodoItem(id: String): Resource<DeleteTodoItemResponse, NetworkError>

}