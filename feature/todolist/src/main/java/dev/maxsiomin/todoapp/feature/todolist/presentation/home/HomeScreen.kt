package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.core.presentation.theme.LocalElevations

@Composable
internal fun HomeScreen(navController: NavHostController) {

    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    HomeScreenContent(state, viewModel::onEvent)

}

@Composable
private fun HomeScreenContent(state: HomeViewModel.State, onEvent: (HomeViewModel.Event) -> Unit) {
    Column {
        Text(text = "Hello from home screen", style = AppTheme.typography.largeTitle)
        Text(text = LocalElevations.current.toString())
    }

}
