package dev.maxsiomin.todoapp.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.todoapp.TodoAppState
import dev.maxsiomin.todoapp.feature.auth.presentation.addAuthNavigation
import dev.maxsiomin.todoapp.feature.todolist.presentation.addTodolistNavigation
import dev.maxsiomin.todoapp.navdestinations.Screen

@Composable
internal fun TodoappNavHost(appState: TodoAppState, showSnackbar: SnackbarCallback) {

    val navController = appState.navController

    val startDestination: Any =
        if (appState.isAuthenticated) Screen.HomeScreen else Screen.AuthScreen

    NavHost(navController = navController, startDestination = startDestination) {
        addAuthNavigation(navController = navController, showSnackbar = showSnackbar)
        addTodolistNavigation(navController = navController, showSnackbar = showSnackbar)
    }

}