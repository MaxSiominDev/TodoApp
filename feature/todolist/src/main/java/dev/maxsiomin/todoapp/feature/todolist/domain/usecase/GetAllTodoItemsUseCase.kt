package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetAllTodoItemsUseCase @Inject constructor(
    private val repo: TodoItemsRepository,
) {

    operator fun invoke(): Flow<Resource<List<TodoItem>, DataError>> {
        return repo.getAllTodoItems()
    }

}