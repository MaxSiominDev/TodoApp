package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.common.domain.resource.DataError
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.common.presentation.UiText
import dev.maxsiomin.common.presentation.asUiText
import dev.maxsiomin.todoapp.core.data.ConnectivityObserver
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.DeleteTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.EditTodoItemUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.GetAllTodoItemsUseCase
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.ScheduleTodoItemsSyncUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ViewModel for HomeScreen */
@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
    private val deleteTodoItemUseCase: DeleteTodoItemUseCase,
    private val editTodoItemUseCase: EditTodoItemUseCase,
    private val scheduleTodoItemsSyncUseCase: ScheduleTodoItemsSyncUseCase,
    private val connectivityObserver: ConnectivityObserver,
    dispatchers: DispatcherProvider,
) : StatefulViewModel<HomeViewModel.State, HomeViewModel.Effect, HomeViewModel.Event>() {

    private var todoItems = emptyList<TodoItem>()

    private var refreshItemsJob: Job? = null

    data class State(
        val todoItems: List<TodoItemUiModel> = emptyList(),
        val completedCount: String = "",
        val hideCompleted: Boolean = false,
    )

    override val _state = MutableStateFlow(State())


    init {
        refreshItems()
        scheduleSync()

        viewModelScope.launch(dispatchers.io) {
            connectivityObserver.observe().collect { status ->
                processConnectivityStatus(status)
            }
        }
    }

    private fun refreshItems() {
        refreshItemsJob?.cancel()
        refreshItemsJob = viewModelScope.launch {
            getAllTodoItemsUseCase().collect { resource ->
                processTodoItemResource(resource)
            }
        }
    }

    private fun processTodoItemResource(resource: Resource<List<TodoItem>, DataError>) {
        when (resource) {
            is Resource.Error -> {
                val message = resource.error.asUiText()
                onEffect(Effect.ShowMessage(message))
                if (todoItems.isNotEmpty()) {
                    val offlineCopyMessage = UiText.StringResource(R.string.offline_copy)
                    onEffect(Effect.ShowMessage(offlineCopyMessage))
                }
            }

            is Resource.Success -> {
                val newItems = resource.data
                processTodoItems(newItems)
            }
        }
    }

    private fun processTodoItems(newItems: List<TodoItem>) {
        todoItems = newItems
        val newIsCompletedCount = newItems.count { it.isCompleted }
        val filteredItemsByIsCompleted = if (state.value.hideCompleted) {
            newItems.filter { !it.isCompleted }
        } else {
            newItems
        }
        _state.update {
            it.copy(
                todoItems = filteredItemsByIsCompleted.map { item -> item.toTodoItemUiModel() },
                completedCount = newIsCompletedCount.toString(),
            )
        }
    }

    private fun scheduleSync() {
        scheduleTodoItemsSyncUseCase()
    }

    private fun processConnectivityStatus(status: ConnectivityObserver.Status) {
        when (status) {
            ConnectivityObserver.Status.Available -> {
                if (connectivityObserver.wasPreviouslyOffline()) {
                    refreshItems()
                }
            }
            ConnectivityObserver.Status.Losing -> Unit
            ConnectivityObserver.Status.Lost -> Unit
            ConnectivityObserver.Status.Unavailable -> Unit
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
        data object Refresh : Event()
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AddClicked -> onEffect(Effect.GoToEditScreen(itemId = null))
            is Event.CheckboxValueChanged -> changeProgress(event.newValue, event.item)
            is Event.EditItem -> onEffect(Effect.GoToEditScreen(itemId = event.item.id))
            is Event.OnCompleteViaDismission -> onCompleteViaDismission(event.item)
            is Event.OnDeleteViaDismission -> onDeleteViaDismission(event.item)
            Event.IconHideCompletedClicked -> iconHideCompletedClicked()
            Event.Refresh -> refreshItems()
        }
    }

    private fun changeProgress(isCompleted: Boolean, todoItem: TodoItemUiModel) {
        viewModelScope.launch {
            val item = todoItems.firstOrNull {
                it.id == todoItem.id
            } ?: return@launch
            val editedItem = item.copy(
                isCompleted = isCompleted,
                modified = System.currentTimeMillis(),
            )
            editTodoItemUseCase(editedItem)
        }
    }

    private fun onDeleteViaDismission(todoItem: TodoItemUiModel) {
        viewModelScope.launch {
            val item = todoItems.firstOrNull {
                it.id == todoItem.id
            } ?: return@launch
            deleteTodoItemUseCase(item)
        }
    }

    private fun onCompleteViaDismission(todoItem: TodoItemUiModel) {
        viewModelScope.launch {
            val item = todoItems.firstOrNull {
                it.id == todoItem.id
            } ?: return@launch
            val completedItem = item.copy(
                isCompleted = true,
                modified = System.currentTimeMillis(),
            )
            editTodoItemUseCase(completedItem)
        }
    }

    private fun iconHideCompletedClicked() {
        _state.update {
            it.copy(hideCompleted = it.hideCompleted.not())
        }
        processTodoItems(todoItems)
    }

}
