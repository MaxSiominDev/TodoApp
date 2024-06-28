package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle

object AppTheme {

    @Composable
    operator fun invoke(
        isDarkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit,
    ) {
        val colorScheme = if (isDarkTheme) darkColors else lightColors
        val typography = appTypography(colorScheme)
        val rippleIndication = rememberRipple()
        CompositionLocalProvider(
            LocalAppColorScheme provides colorScheme,
            LocalAppTypography provides typography,
            LocalIndication provides rippleIndication,
        ) {
            content()
        }

        // Cast will fail on Preview
        val context = LocalContext.current as? ComponentActivity ?: return
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
