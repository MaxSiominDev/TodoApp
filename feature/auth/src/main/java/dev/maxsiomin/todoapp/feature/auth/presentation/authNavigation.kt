package dev.maxsiomin.todoapp.feature.auth.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.maxsiomin.todoapp.navdestinations.Screen

fun NavGraphBuilder.addAuthNavigation() {

    composable<Screen.AuthScreen> {
        AuthScreen()
    }

}
