package dev.maxsiomin.common.util

import android.app.Activity
import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat

fun setStatusBarColor(context: Context, color: Color, darkIcons: Boolean) {
    val activity = context as Activity
    val window = activity.window

    window.statusBarColor = color.toArgb()

    WindowCompat.getInsetsController(window, window.decorView).isAppearanceLightStatusBars = darkIcons
}