package dev.maxsiomin.todoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberTodoAppState(
    isAuthenticated: Boolean,
    navController: NavHostController = rememberNavController(),
): TodoAppState {
    return remember(navController) {
        TodoAppState(
            navController = navController,
            isAuthenticated = isAuthenticated,
        )
    }
}

/** Represents state of the application */
@Stable
class TodoAppState(
    val navController: NavHostController,
    val isAuthenticated: Boolean,
)
