package dev.maxsiomin.todoapp

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.ui.TodoApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                val appState = rememberTodoAppState()
                TodoApp(appState = appState)
            }
        }
    }

}
