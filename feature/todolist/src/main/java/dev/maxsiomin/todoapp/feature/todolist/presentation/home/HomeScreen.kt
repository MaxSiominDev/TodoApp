package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
internal fun HomeScreen(navController: NavHostController) {

    val viewModel: HomeViewModel = hiltViewModel()
    HomeScreenContent()

}

@Composable
private fun HomeScreenContent() {
    Text(text = "Hello from home screen")

}
