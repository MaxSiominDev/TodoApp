package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AppThemeComposable(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (isDarkTheme) darkColors else lightColors
    val elevations = Elevations(card = 1.dp, default = 1.dp)
    CompositionLocalProvider(
        LocalAppColorScheme provides colorScheme,
        LocalAppTypography provides appTypography,
        LocalElevations provides elevations
    ) {
        content()
    }
}

object AppTheme {

    val colors: AppColorScheme
        @Composable get() {
            val scheme = LocalAppColorScheme.current
            return scheme
        }

    val typography
        @Composable get() = LocalAppTypography.current

}

val LocalAppColorScheme = compositionLocalOf {
    AppColorScheme(
        support = Color.Unspecified
    )
}

val LocalAppTypography = compositionLocalOf {
    AppTypography(
        largeTitle = TextStyle.Default,
        title = TextStyle.Default,
        button = TextStyle.Default,
        body = TextStyle.Default,
        subhead = TextStyle.Default,
    )
}

data class Elevations(val card: Dp = 0.dp, val default: Dp = 0.dp)

// Define a CompositionLocal global object with a default
// This instance can be accessed by all composables in the app
val LocalElevations = compositionLocalOf { Elevations() }
