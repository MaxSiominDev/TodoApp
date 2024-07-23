package dev.maxsiomin.todoapp.feature.todolist.presentation.edit

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        onDismissRequest = onDismissRequest
    ) {

        ItemRow(
            text = stringResource(id = R.string.low_priority),
            onClick = {
                onSelect(Priority.Low)
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
            text = stringResource(id = R.string.default_priority),
            onClick = {
                onSelect(Priority.Default)
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
        
        AnimatableHighPriorityRow(onSelect = onSelect)

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
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = text, style = AppTheme.typography.body)
        }
    }
}

@Composable
private fun AnimatableHighPriorityRow(onSelect: (Priority) -> Unit) {
    val animationTime = 500

    var isClicked by rememberSaveable {
        mutableStateOf(false)
    }
    LaunchedEffect(isClicked) {
        if (isClicked) {
            delay(animationTime / 2L) // Wait for the color to change to red
            isClicked = false // Change color back to white
        }
    }

    val initialColor = AppTheme.colors.backSecondary
    val color by animateColorAsState(
        targetValue = if (isClicked) AppTheme.colors.colorRed else initialColor,
        animationSpec = tween(durationMillis = animationTime / 2),
        label = "High importance anim",
    )
    ItemRow(
        modifier = Modifier
            .drawBehind { drawRect(color) },
        text = stringResource(id = R.string.high_priority),
        onClick = {
            onSelect(Priority.High)
            isClicked = true
        },
        icon = {
            Icon(
                tint = AppTheme.colors.colorRed,
                painter = painterResource(R.drawable.icon_priority_high),
                contentDescription = stringResource(R.string.high_priority),
            )
        }
    )
}

