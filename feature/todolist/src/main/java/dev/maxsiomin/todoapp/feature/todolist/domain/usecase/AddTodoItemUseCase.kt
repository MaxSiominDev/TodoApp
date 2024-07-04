package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddTodoItemUseCase @Inject constructor(
    private val repo: TodoItemsRepository,
    private val dispatchers: DispatcherProvider,
) {

    suspend operator fun invoke(
        todoItem: TodoItem
    ): Unit = withContext(dispatchers.io) {
        repo.addTodoItem(todoItem)
    }

}