package dev.maxsiomin.todoapp.feature.todolist.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.todoapp.feature.todolist.presentation.edit.EditScreen
import dev.maxsiomin.todoapp.feature.todolist.presentation.home.HomeScreen
import dev.maxsiomin.todoapp.navdestinations.Screen

fun NavGraphBuilder.addTodolistNavigation(
    navController: NavHostController,
    showSnackbar: SnackbarCallback
) {

    composable<Screen.HomeScreen> {
        HomeScreen(
           goToEditScreen = { itemId: String? ->
               navController.navigate(Screen.EditScreen(itemId = itemId))
           },
            goToSettingsScreen = {
                navController.navigate(Screen.SettingsScreen)
            },
            showSnackbar = showSnackbar,
        )
    }

    composable<Screen.EditScreen> {
        EditScreen(navController = navController, showSnackbar = showSnackbar)
    }

}