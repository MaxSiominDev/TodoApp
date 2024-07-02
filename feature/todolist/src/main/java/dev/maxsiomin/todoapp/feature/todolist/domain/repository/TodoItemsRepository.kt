package dev.maxsiomin.todoapp.feature.todolist.domain.repository

import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

internal interface TodoItemsRepository {

    fun getAllTodoItems(): Flow<Resource<List<TodoItem>, DataError>>

    suspend fun addTodoItem(item: TodoItem)

    suspend fun editTodoItem(item: TodoItem)

    suspend fun getTodoItemById(id: String): Resource<TodoItem, DataError>

    suspend fun deleteTodoItem(item: TodoItem)

}