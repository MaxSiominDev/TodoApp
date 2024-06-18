package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import dev.maxsiomin.common.presentation.StatefulViewModel
import kotlinx.coroutines.flow.MutableStateFlow

internal class HomeViewModel : StatefulViewModel<HomeViewModel.State, Nothing, HomeViewModel.Event>() {

    class State

    override val _state = MutableStateFlow(State())

    sealed class Event {

    }

    override fun onEvent(event: Event) {

    }

}