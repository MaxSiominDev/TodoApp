package dev.maxsiomin.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.ui.TodoApp

/** Single activity  */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val isAuthenticated = viewModel.isAuthenticated()
            AppTheme {
                val appState = rememberTodoAppState(isAuthenticated = isAuthenticated)
                TodoApp(appState = appState)
            }
        }
    }

}
