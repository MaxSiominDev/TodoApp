package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    val support: Color,
)

internal val lightColors = AppColorScheme(
    support = Color.Red//Color(0x33000000)
)

internal val darkColors = AppColorScheme(
    support = Color.Green
)
