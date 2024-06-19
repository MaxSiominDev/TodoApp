package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.common.presentation.UiText
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Progress
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import dev.maxsiomin.todoapp.feature.todolist.domain.usecase.GetAllTodoItemsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val getAllTodoItemsUseCase: GetAllTodoItemsUseCase,
) : StatefulViewModel<HomeViewModel.State, HomeViewModel.Effect, HomeViewModel.Event>() {

    data class State(
        val todoItems: List<TodoItem> = emptyList(),
        val completedCount: String = "",
    )

    override val _state = MutableStateFlow(State())


    init {
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
                        val newCount = newItems.count { item -> item.progress == Progress.Completed }
                        _state.update {
                            it.copy(
                                todoItems = resource.data,
                                completedCount = newCount.toString(),
                            )
                        }
                    }
                }
            }
        }
    }


    sealed class Effect {
        data class GoToEditScreen(val itemId: String?) : Effect()
        data class ShowMessage(val message: UiText) : Effect()
    }


    sealed class Event {
        data class CheckboxValueChanged(val newValue: Boolean) : Event()
        data object AddClicked : Event()
    }

    override fun onEvent(event: Event) {
        when (event) {
            Event.AddClicked -> onEffect(Effect.GoToEditScreen(itemId = null))
            is Event.CheckboxValueChanged -> TODO()
        }
    }

}