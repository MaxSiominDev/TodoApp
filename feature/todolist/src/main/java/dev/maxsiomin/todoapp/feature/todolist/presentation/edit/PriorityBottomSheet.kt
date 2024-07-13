package dev.maxsiomin.todoapp.feature.todolist.presentation.edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PriorityBottomSheet(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onSelect: (Priority) -> Unit,
) {

    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        containerColor = AppTheme.colors.backSecondary,
        modifier = modifier,
        sheetState = sheetState,
        onDismissRequest = { onDismissRequest() }
    ) {

        ItemRow(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.low_priority),
            onClick = {
                onSelect(Priority.Low)
                onDismissRequest()
            },
            icon = {
                Icon(
                    tint = AppTheme.colors.colorGray,
                    painter = painterResource(R.drawable.icon_priority_low),
                    contentDescription = null,
                )
            }
        )

        ItemRow(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.default_priority),
            onClick = {
                onSelect(Priority.Default)
                onDismissRequest()
            },
            icon = {
                Icon(
                    modifier = Modifier.size(width = 16.dp, height = 20.dp),
                    tint = AppTheme.colors.colorGray,
                    // Chat GPT recommended to use this icon here
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                )
            }
        )

        ItemRow(
            modifier = Modifier.padding(16.dp),
            text = stringResource(id = R.string.high_priority),
            onClick = {
                onSelect(Priority.High)
                onDismissRequest()
            },
            icon = {
                Icon(
                    tint = AppTheme.colors.colorRed,
                    painter = painterResource(R.drawable.icon_priority_high),
                    contentDescription = stringResource(R.string.high_priority),
                )
            }
        )

        Spacer(modifier = Modifier.height(48.dp))

    }

}

@Composable
private fun ItemRow(
    text: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.clickable { onClick() }) {
        icon()
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = AppTheme.typography.body)
    }
}
