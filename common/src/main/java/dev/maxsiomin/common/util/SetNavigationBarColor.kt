package dev.maxsiomin.common.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**@Composable
fun SetNavigationBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    if (darkTheme) {
        systemUiController.setSystemBarsColor(
            color = Color.Black
        )
    } else {
        systemUiController.setSystemBarsColor(
            color = RoyalBlue
        )
    }
}*/