package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class GetTodoItemByIdUseCase @Inject constructor(
    private val repo: TodoItemsRepository,
    private val dispatchers: DispatcherProvider,
) {

    suspend operator fun invoke(id: String): Resource<TodoItem, DataError> =
        withContext(dispatchers.io) {
            return@withContext repo.getTodoItemById(id)
        }

}