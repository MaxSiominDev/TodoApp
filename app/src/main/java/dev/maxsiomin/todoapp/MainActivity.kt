package dev.maxsiomin.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.ui.TodoApp

/** Single activity  */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()

            val isSystemInDarkTheme = isSystemInDarkTheme()
            val isDarkMode = remember(state.theme) {
                when (state.theme) {
                    Theme.Dark -> true
                    Theme.Light -> false
                    Theme.SystemDefault -> isSystemInDarkTheme
                }
            }

            AppTheme(isDarkTheme = isDarkMode) {
                val appState = rememberTodoAppState(isAuthenticated = state.isAuthenticated)
                TodoApp(appState = appState)
            }

        }
    }

}
