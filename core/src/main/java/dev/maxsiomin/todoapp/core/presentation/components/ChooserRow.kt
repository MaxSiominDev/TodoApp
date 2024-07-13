package dev.maxsiomin.todoapp.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.core.presentation.theme.PreviewConfig
import dev.maxsiomin.todoapp.core.presentation.theme.PreviewConfigProvider

@Composable
fun ChooserRow(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    background: Color = AppTheme.colors.backPrimary,
    onClick: () -> Unit,
) {
    Row(
        modifier
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp)
            .background(background),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text = text, style = AppTheme.typography.body)
    }
}

@Preview
@Composable
private fun ChooserRowPreview(
    @PreviewParameter(PreviewConfigProvider::class) config: PreviewConfig,
) {
    AppTheme(isDarkTheme = config.isDarkTheme) {
        Column(Modifier.background(AppTheme.colors.backPrimary)) {
            ChooserRow(text = "Hello world", selected = false, onClick = {})
            ChooserRow(text = "Hello world", selected = true, onClick = {})
        }
    }
}
