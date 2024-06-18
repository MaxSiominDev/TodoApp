package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

data class AppTypography(
    val largeTitle: TextStyle,
    val title: TextStyle,
    val button: TextStyle,
    val body: TextStyle,
    val subhead: TextStyle,
)

internal fun appTypography(colorScheme: AppColorScheme): AppTypography {
    return AppTypography(
        largeTitle = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 38.sp,
            letterSpacing = 0.5.sp,
            color = colorScheme.support,
        ),
        title = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.5.sp,
            color = Color.Unspecified,
        ),
        button = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp,
            color = Color.Unspecified,
        ),
        body = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.5.sp,
            color = Color.Unspecified,
        ),
        subhead = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.5.sp,
            color = Color.Unspecified,
        ),
    )
}