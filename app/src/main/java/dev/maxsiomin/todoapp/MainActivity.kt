package dev.maxsiomin.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.todoapp.core.presentation.theme.AppThemeComposable
import dev.maxsiomin.todoapp.ui.TodoApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppThemeComposable {
                val appState = rememberTodoAppState()
                TodoApp(appState = appState)
            }
        }
    }

}
