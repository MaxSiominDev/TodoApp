package dev.maxsiomin.todoapp.core.presentation.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
private fun ColorPalettePreview() {
    val colorScheme = AppTheme.colors
    Column(modifier = Modifier.padding(16.dp)) {
        listOf(
            "supportSeparator" to colorScheme.supportSeparator,
            "supportOverlay" to colorScheme.supportOverlay,
            "labelPrimary" to colorScheme.labelPrimary,
            "labelSecondary" to colorScheme.labelSecondary,
            "labelTertiary" to colorScheme.labelTertiary,
            "labelDisable" to colorScheme.labelDisable,
            "colorRed" to colorScheme.colorRed,
            "colorGreen" to colorScheme.colorGreen,
            "colorBlue" to colorScheme.colorBlue,
            "colorGray" to colorScheme.colorGray,
            "colorGrayLight" to colorScheme.colorGrayLight,
            "colorWhite" to colorScheme.colorWhite,
            "backPrimary" to colorScheme.backPrimary,
            "backSecondary" to colorScheme.backSecondary,
            "backElevated" to colorScheme.backElevated
        ).forEach { (name, color) ->
            ColorBox(name = name, color = color)
        }
    }
}

@Composable
private fun ColorBox(name: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 4.dp)) {
        Box(modifier = Modifier
            .size(40.dp)
            .background(color))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun TypographyPreview(modifier: Modifier = Modifier) {
    val typography = AppTheme.typography
    Column(modifier = modifier.padding(16.dp)) {
        Text(text = "Large Title", style = typography.largeTitle)
        Text(text = "Title", style = typography.title)
        Text(text = "Button", style = typography.button)
        Text(text = "Body", style = typography.body)
        Text(text = "Subhead", style = typography.subhead)
    }
}

@Preview(showBackground = true)
@Composable
private fun LightThemeColorPreview() {
    AppTheme(isDarkTheme = false) {
        ColorPalettePreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun DarkThemeColorPreview() {
    AppTheme(isDarkTheme = true) {
        ColorPalettePreview()
    }
}

@Preview(showBackground = true)
@Composable
private fun LightThemeTypographyPreview() {
    AppTheme(isDarkTheme = false) {
        TypographyPreview(Modifier.background(Color.White))
    }
}

@Preview(showBackground = true)
@Composable
private fun DarkThemeTypographyPreview() {
    AppTheme(isDarkTheme = true) {
        TypographyPreview(Modifier.background(Color.Black))
    }
}
