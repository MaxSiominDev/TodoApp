package dev.maxsiomin.todoapp.ui

import androidx.compose.runtime.Composable
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.todoapp.TodoAppState

@Composable
fun TodoappNavHost(appState: TodoAppState, showSnackbar: SnackbarCallback) {

    val navController = appState.navController



}