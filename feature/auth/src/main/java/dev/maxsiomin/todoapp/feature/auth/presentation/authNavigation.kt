package dev.maxsiomin.todoapp.feature.auth.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.todoapp.navdestinations.Screen

fun NavGraphBuilder.addAuthNavigation(
    navController: NavHostController,
    showSnackbar: SnackbarCallback
) {

    composable<Screen.AuthScreen> {
        AuthScreen(navController = navController, showSnackbar = showSnackbar)
    }

}
