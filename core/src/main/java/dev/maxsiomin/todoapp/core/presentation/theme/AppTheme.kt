package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

object AppTheme {

    @Composable
    operator fun invoke(
        isDarkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit,
    ) {
        val colorScheme = if (isDarkTheme) darkColors else lightColors
        val typography = appTypography(colorScheme)
        CompositionLocalProvider(
            LocalAppColorScheme provides colorScheme,
            LocalAppTypography provides typography,
        ) {
            content()
        }
    }

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
