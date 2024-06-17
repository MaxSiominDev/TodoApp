package dev.maxsiomin.todoapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberTodoAppState(
    navController: NavHostController = rememberNavController(),
): TodoAppState {
    return remember(navController) {
        TodoAppState(
            navController = navController,
        )
    }
}

@Stable
class TodoAppState(
    val navController: NavHostController,
)
