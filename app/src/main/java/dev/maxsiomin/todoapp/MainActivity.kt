package dev.maxsiomin.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dagger.hilt.android.AndroidEntryPoint
import dev.maxsiomin.todoapp.ui.TodoApp
import dev.maxsiomin.todoapp.core.presentation.theme.TodoAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            dev.maxsiomin.todoapp.core.presentation.theme.TodoAppTheme {
                val appState = rememberTodoAppState()
                TodoApp(appState = appState)
            }
        }
    }

}
