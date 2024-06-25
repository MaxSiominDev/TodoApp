package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

data class PreviewConfig(
    val isDarkTheme: Boolean,
)

class PreviewConfigProvider : PreviewParameterProvider<PreviewConfig> {
    override val values = sequenceOf(
        PreviewConfig(isDarkTheme = false),
        PreviewConfig(isDarkTheme = true),
    )
}
