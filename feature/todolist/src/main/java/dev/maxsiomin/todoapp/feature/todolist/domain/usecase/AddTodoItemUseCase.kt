package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

internal class AddTodoItemUseCase @Inject constructor(
    private val repo: TodoItemsRepository
) {

    suspend operator fun invoke(todoItem: TodoItem) = withContext(Dispatchers.IO) {
        repo.addTodoItem(todoItem)
    }

}