package dev.maxsiomin.todoapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.todoapp.TodoAppState
import dev.maxsiomin.todoapp.feature.auth.presentation.addAuthNavigation
import dev.maxsiomin.todoapp.feature.todolist.presentation.addTodolistNavigation
import dev.maxsiomin.todoapp.navdestinations.Screen

@Composable
fun TodoappNavHost(appState: TodoAppState, showSnackbar: SnackbarCallback) {

    val navController = appState.navController
    NavHost(navController = navController, startDestination = Screen.HomeScreen) {
        addAuthNavigation()
        addTodolistNavigation(navController = navController, showSnackbar = showSnackbar)
    }

}