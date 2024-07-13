package dev.maxsiomin.todoapp.feature.settings.presentation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import dev.maxsiomin.todoapp.feature.settings.presentation.settings.SettingsScreen
import dev.maxsiomin.todoapp.navdestinations.Screen

fun NavGraphBuilder.addSettingNavigation(
    navController: NavHostController,
) {

    composable<Screen.SettingsScreen> {
        SettingsScreen(navController = navController)
    }

}
