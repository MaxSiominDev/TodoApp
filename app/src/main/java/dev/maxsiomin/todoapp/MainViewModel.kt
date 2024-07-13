package dev.maxsiomin.todoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.domain.usecase.GetThemeFlowUseCase
import dev.maxsiomin.todoapp.core.domain.usecase.GetThemeUseCase
import dev.maxsiomin.todoapp.core.domain.usecase.GetTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/** Viewmodel for [MainActivity] */
@HiltViewModel
internal class MainViewModel @Inject constructor(
    private val getTokenUseCase: GetTokenUseCase,
    private val getThemeUseCase: GetThemeUseCase,
    private val getThemeFlowUseCase: GetThemeFlowUseCase,
) : ViewModel() {

    data class State(
        val isAuthenticated: Boolean,
        val theme: Theme,
    )

    private var _state: MutableStateFlow<State>
    val state: StateFlow<State>

    init {
        val currTheme = getThemeUseCase()
        val stateValue = State(
            isAuthenticated = isAuthenticated(),
            theme = currTheme,
        )
        _state = MutableStateFlow(stateValue)
        state = _state

        viewModelScope.launch {
            getThemeFlowUseCase().collect { newTheme ->
                _state.update {
                    it.copy(theme = newTheme)
                }
            }
        }
    }

    private fun isAuthenticated() = getTokenUseCase() != null

}
