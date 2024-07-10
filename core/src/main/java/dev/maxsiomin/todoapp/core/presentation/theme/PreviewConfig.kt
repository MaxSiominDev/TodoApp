package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

/** Config for previews */
data class PreviewConfig(
    val isDarkTheme: Boolean,
)

/** Default [PreviewConfig] provider */
class PreviewConfigProvider : PreviewParameterProvider<PreviewConfig> {
    override val values = sequenceOf(
        PreviewConfig(isDarkTheme = false),
        PreviewConfig(isDarkTheme = true),
    )
}
