package dev.maxsiomin.todoapp.navdestinations

import kotlinx.serialization.Serializable

/** Containts all nav destionations in project */
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

}
