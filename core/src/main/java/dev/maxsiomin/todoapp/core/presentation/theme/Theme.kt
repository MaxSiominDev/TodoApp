package dev.maxsiomin.todoapp.core.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),
    secondary = Color(0xFF32D74B),
    background = Color(0xFF161618),
    surface = Color(0xFF252528),
    error = Color(0xFFFF453A),
    onPrimary = Color.White,
    onSecondary = Color(0x99FFFFFF),
    onBackground = Color(0x66FFFFFF),
    onSurface = Color(0x26FFFFFF),
    onError = Color(0x52000000),
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),
    secondary = Color(0xFF34C759),
    background = Color(0xFFF7F6F2),
    surface = Color.White,
    error = Color(0xFFFF3B30),
    onPrimary = Color.Black,
    onSecondary = Color(0x99000000),
    onBackground = Color(0x4D000000),
    onSurface = Color(0x26000000),
    onError = Color(0x0F000000),
)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}