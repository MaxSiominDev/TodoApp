package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

/** Deletes a todo item */
internal class DeleteTodoItemUseCase @Inject constructor(
    private val repo: TodoItemsRepository,
    private val dispatchers: DispatcherProvider,
) {

    suspend operator fun invoke(todoItem: TodoItem) = withContext(dispatchers.io) {
        repo.deleteTodoItem(todoItem)
    }

}