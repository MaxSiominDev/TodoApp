package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.common.presentation.UiText
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Progress
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.AddTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.DeleteTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.GetAllTodoItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val addTodoItemUseCase: AddTodoItemUseCase,
) : StatefulViewModel<HomeViewModel.State, HomeViewModel.Effect, HomeViewModel.Event>() {

    private var items = emptyList<TodoItem>()

    data class State(
        val todoItems: List<TodoItemUiModel> = emptyList(),
        val completedCount: String = "",
        val hideCompleted: Boolean = false,
    )

    override val _state = MutableStateFlow(State())


    init {
        refreshItems()
    }

    private fun refreshItems() {
        viewModelScope.launch {
            getAllTodoItemsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Error -> onEffect(
                        Effect.ShowMessage(
                            UiText.StringResource(R.string.unknown_error)
                        )
                    )

                    is Resource.Success -> {
                        val newItems = resource.data
                        processTodoItems(newItems)
                    }
                }
            }
        }
    }

    private fun processTodoItems(newItems: List<TodoItem>) {
        items = newItems
        val newCount = newItems.count { item -> item.progress == Progress.Completed }
        val filteredItemsByIsCompleted = if (state.value.hideCompleted) {
            newItems.filter { it.progress == Progress.NotCompleted }
        } else {
            newItems
        }
        _state.update {
            it.copy(
                todoItems = filteredItemsByIsCompleted.map { item -> item.toTodoItemUiModel() },
                completedCount = newCount.toString(),
            )
        }
    }


    sealed class Effect {
        data class GoToEditScreen(val itemId: String?) : Effect()
        data class ShowMessage(val message: UiText) : Effect()
    }


    sealed class Event {
        data class CheckboxValueChanged(val newValue: Boolean, val item: TodoItemUiModel) : Event()
        data object AddClicked : Event()
        data class EditItem(val item: TodoItemUiModel) : Event()
        data class OnDeleteViaDismission(val item: TodoItemUiModel) : Event()
        data class OnCompleteViaDismission(val item: TodoItemUiModel) : Event()
        data object IconHideCompletedClicked : Event()
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AddClicked -> onEffect(Effect.GoToEditScreen(itemId = null))
            is Event.CheckboxValueChanged -> changeProgress(event.newValue, event.item)
            is Event.EditItem -> onEffect(Effect.GoToEditScreen(itemId = event.item.id))
            is Event.OnCompleteViaDismission -> onCompleteViaDismission(event.item)
            is Event.OnDeleteViaDismission -> onDeleteViaDismission(event.item)
            Event.IconHideCompletedClicked -> iconHideCompletedClicked()
        }
    }

    private fun changeProgress(booleanValue: Boolean, todoItem: TodoItemUiModel) {
        viewModelScope.launch {
            val progress = if (booleanValue) Progress.Completed else Progress.NotCompleted
            val item = items.firstOrNull {
                it.id == todoItem.id
            } ?: return@launch
            val editedItem = item.copy(progress = progress)
            addTodoItemUseCase(editedItem)
        }
    }

    private fun onDeleteViaDismission(todoItem: TodoItemUiModel) {
        viewModelScope.launch {
            val item = items.firstOrNull {
                it.id == todoItem.id
            } ?: return@launch
            deleteTodoItemUseCase(item)
        }
    }

    private fun onCompleteViaDismission(todoItem: TodoItemUiModel) {
        viewModelScope.launch {
            val item = items.firstOrNull {
                it.id == todoItem.id
            } ?: return@launch
            val completedItem = item.copy(progress = Progress.Completed)
            addTodoItemUseCase(completedItem)
        }
    }

    private fun iconHideCompletedClicked() {
        _state.update {
            it.copy(hideCompleted = it.hideCompleted.not())
        }
        processTodoItems(items)
    }

}
