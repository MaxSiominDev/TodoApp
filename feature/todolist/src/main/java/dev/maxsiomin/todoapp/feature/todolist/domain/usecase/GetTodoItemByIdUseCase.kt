package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetTodoItemByIdUseCase @Inject constructor(
    private val repo: TodoItemsRepository,
) {

    suspend operator fun invoke(id: String): Resource<TodoItem, DataError> =
        withContext(Dispatchers.IO) {
            return@withContext repo.getTodoItemById(id)
        }

}