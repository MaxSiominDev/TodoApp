package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import dev.maxsiomin.common.util.setStatusBarColor

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
        val context = LocalContext.current as ComponentActivity
        if (isDarkTheme) {
            context.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.dark(colorScheme.backPrimary.toArgb()),
                navigationBarStyle = SystemBarStyle.dark(colorScheme.backPrimary.toArgb())
            )
        } else {
            context.enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.light(
                    colorScheme.backPrimary.toArgb(),
                    colorScheme.colorGray.toArgb()
                ),
                navigationBarStyle = SystemBarStyle.light(
                    colorScheme.backPrimary.toArgb(),
                    colorScheme.colorGray.toArgb()
                )
            )
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

val LocalAppColorScheme = staticCompositionLocalOf {
    AppColorScheme(
        supportSeparator = Color.Unspecified,
        supportOverlay = Color.Unspecified,
        labelPrimary = Color.Unspecified,
        labelSecondary = Color.Unspecified,
        labelTertiary = Color.Unspecified,
        labelDisable = Color.Unspecified,
        colorRed = Color.Unspecified,
        colorGreen = Color.Unspecified,
        colorBlue = Color.Unspecified,
        colorGray = Color.Unspecified,
        colorGrayLight = Color.Unspecified,
        colorWhite = Color.Unspecified,
        backPrimary = Color.Unspecified,
        backSecondary = Color.Unspecified,
        backElevated = Color.Unspecified,
    )
}

val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        largeTitle = TextStyle.Default,
        title = TextStyle.Default,
        button = TextStyle.Default,
        body = TextStyle.Default,
        subhead = TextStyle.Default,
    )
}
