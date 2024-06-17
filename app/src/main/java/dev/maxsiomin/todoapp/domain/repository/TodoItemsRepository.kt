package dev.maxsiomin.todoapp.domain.repository

import dev.maxsiomin.todoapp.common.domain.resource.DataError
import dev.maxsiomin.todoapp.common.domain.resource.Resource
import dev.maxsiomin.todoapp.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

interface TodoItemsRepository {

    fun getAllTodoItems(): Flow<Resource<List<TodoItem>, DataError>>

    suspend fun addTodoItem(item: TodoItem)

}