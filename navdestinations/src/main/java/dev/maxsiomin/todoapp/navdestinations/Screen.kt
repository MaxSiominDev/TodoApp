package dev.maxsiomin.todoapp.navdestinations

import kotlinx.serialization.Serializable

/** Contains all nav destinations in project */
sealed class Screen {

    @Serializable
    data object AuthScreen : Screen()

    @Serializable
    data object HomeScreen : Screen()

    @Serializable
    data class EditScreen(val itemId: String?) : Screen() {
        object Args {
            val itemId = "itemId"
        }
    }

    @Serializable
    data object SettingsScreen : Screen()

}
