package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
internal class HomeViewModel @Inject constructor(
    private val repo: TodoItemsRepository,
): StatefulViewModel<HomeViewModel.State, Nothing, HomeViewModel.Event>() {

    class State

    override val _state = MutableStateFlow(State())

    sealed class Event {
        data class CheckboxValueChanged(val newValue: Boolean) : Event()
    }

    override fun onEvent(event: Event) {

    }

}