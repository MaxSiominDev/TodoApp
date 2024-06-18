package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.ui.graphics.Color

data class AppColorScheme(
    val supportSeparator: Color,
    val supportOverlay: Color,
    val labelPrimary: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelDisable: Color,
    val colorRed: Color,
    val colorGreen: Color,
    val colorBlue: Color,
    val colorGray: Color,
    val colorGrayLight: Color,
    val colorWhite: Color,
    val backPrimary: Color,
    val backSecondary: Color,
    val backElevated: Color,
)

internal val lightColors = AppColorScheme(
    supportSeparator = Color(0x33000000),
    supportOverlay = Color(0xF0000000),
    labelPrimary = Color(0xFF000000),
    labelSecondary = Color(0x99000000),
    labelTertiary = Color(0x4D000000),
    labelDisable = Color(0x26000000),
    colorRed = Color(0xFFFF3B30),
    colorGreen = Color(0xFF34C759),
    colorBlue = Color(0xFF007AFF),
    colorGray = Color(0xFF8E8E93),
    colorGrayLight = Color(0xFFD1D1D6),
    colorWhite = Color(0xFFFFFFFF),
    backPrimary = Color(0xFFF7F6F2),
    backSecondary = Color(0xFFFFFFFF),
    backElevated = Color(0xFFFFFFFF),
)

internal val darkColors = AppColorScheme(
    supportSeparator = Color(0x33FFFFFF),
    supportOverlay = Color(0x52000000),
    labelPrimary = Color(0xFFFFFFFF),
    labelSecondary = Color(0x99FFFFFF),
    labelTertiary = Color(0x66FFFFFF),
    labelDisable = Color(0x26FFFFFF),
    colorRed = Color(0xFFFF453A),
    colorGreen = Color(0xFF32D74B),
    colorBlue = Color(0xFF0A84FF),
    colorGray = Color(0xFF8E8E93),
    colorGrayLight = Color(0xFF48484A),
    colorWhite = Color(0xFFFFFFFF),
    backPrimary = Color(0xFF161618),
    backSecondary = Color(0xFF252528),
    backElevated = Color(0xFF3C3C3F),
)

