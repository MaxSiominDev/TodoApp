package dev.maxsiomin.todoapp.feature.settings.presentation.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.domain.usecase.GetThemeFlowUseCase
import dev.maxsiomin.todoapp.core.domain.usecase.SetThemeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getThemeFlowUseCase: GetThemeFlowUseCase,
    private val setThemeUseCase: SetThemeUseCase,
) : StatefulViewModel<SettingsViewModel.State, SettingsViewModel.Effect, SettingsViewModel.Event>() {

    data class State(
        val selectedTheme: Theme? = null,
    )

    override val _state = MutableStateFlow(State())

    init {
        viewModelScope.launch {
            getThemeFlowUseCase().collect { theme ->
                _state.update {
                    it.copy(selectedTheme = theme)
                }
            }
        }
    }

    sealed class Effect {
        data object GoBack : Effect()
    }

    sealed class Event {
        data class ThemeChanged(val newTheme: Theme) : Event()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.ThemeChanged -> onThemeChanged(event.newTheme)
        }
    }

    private fun onThemeChanged(newTheme: Theme) {
        setThemeUseCase.invoke(newTheme)
    }

}
