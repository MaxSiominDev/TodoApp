package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import android.content.res.Configuration
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Progress

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun TodoItemComposable(
    todoItem: TodoItemUiModel,
    onEvent: (HomeViewModel.Event) -> Unit,
    modifier: Modifier = Modifier
) {

    val currentItem = rememberUpdatedState(todoItem)
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            when (it) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    onEvent(HomeViewModel.Event.OnCompleteViaDismission(currentItem.value))
                    false
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    onEvent(HomeViewModel.Event.OnDeleteViaDismission(currentItem.value))
                    false
                }

                SwipeToDismissBoxValue.Settled -> false
            }
        }
    )
    val notCompleted = todoItem.progress == Progress.NotCompleted
    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            BackgroundForSwipeToDismiss(dismissState = dismissState)
        },
        enableDismissFromStartToEnd = notCompleted,
        enableDismissFromEndToStart = true,
    ) {
        Box(modifier = modifier.fillMaxWidth()) {
            TodoItemComposableContent(todoItem, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackgroundForSwipeToDismiss(dismissState: SwipeToDismissBoxState) {
    val color by animateColorAsState(
        targetValue = when (dismissState.targetValue) {
            SwipeToDismissBoxValue.Settled -> Color.Transparent
            SwipeToDismissBoxValue.StartToEnd -> Color.Green
            SwipeToDismissBoxValue.EndToStart -> Color.Red
        },
        label = "swipeToDismissBackground"
    )

    Box(
        Modifier
            .fillMaxSize()
            .background(color)
    ) {
        when (dismissState.targetValue) {
            SwipeToDismissBoxValue.Settled -> Unit
            SwipeToDismissBoxValue.StartToEnd -> {
                IconMarkCompletedForSwipeToDismiss()
            }

            SwipeToDismissBoxValue.EndToStart -> {
                IconDeleteForSwipeToDismiss()
            }
        }
    }
}

@Composable
private fun BoxScope.IconMarkCompletedForSwipeToDismiss() {
    Icon(
        tint = AppTheme.colors.colorWhite,
        modifier = Modifier
            .align(Alignment.CenterStart)
            .padding(start = 16.dp),
        painter = painterResource(R.drawable.icon_check),
        contentDescription = stringResource(R.string.mark_completed),
    )
}

@Composable
private fun BoxScope.IconDeleteForSwipeToDismiss() {
    Icon(
        tint = AppTheme.colors.colorWhite,
        modifier = Modifier
            .align(Alignment.CenterEnd)
            .padding(end = 16.dp),
        painter = painterResource(R.drawable.icon_delete),
        contentDescription = stringResource(R.string.delete),
    )
}

@Composable
private fun TodoItemComposableContent(
    todoItem: TodoItemUiModel,
    onEvent: (HomeViewModel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val isChecked = when (todoItem.progress) {
            Progress.Completed -> true
            Progress.NotCompleted -> false
        }
        val uncheckedColor = if (todoItem.priority == Priority.High) {
            AppTheme.colors.colorRed
        } else {
            AppTheme.colors.supportSeparator
        }
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onEvent(
                    HomeViewModel.Event.CheckboxValueChanged(newValue = it, item = todoItem)
                )
            },
            colors = CheckboxDefaults.colors(
                checkedColor = AppTheme.colors.colorGreen,
                uncheckedColor = uncheckedColor,
            ),
        )

        if (todoItem.progress == Progress.NotCompleted) {
            when (todoItem.priority) {
                Priority.Default -> Unit

                Priority.High -> {
                    HighPriorityIcon()
                }

                Priority.Low -> {
                    LowPriorityIcon()
                }
            }
        }

        Column(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .weight(1f),
        ) {
            DescriptionText(todoItem)
            if (todoItem.deadline != null) {
                Text(
                    modifier = Modifier.padding(4.dp),
                    text = todoItem.deadline,
                    style = AppTheme.typography.subhead,
                )
            }
        }

        IconButton(onClick = {
            onEvent(HomeViewModel.Event.EditItem(todoItem))
        }) {
            Icon(
                tint = AppTheme.colors.labelTertiary,
                painter = painterResource(id = R.drawable.icon_info),
                contentDescription = stringResource(id = R.string.info),
            )
        }
    }
}

@Composable
private fun HighPriorityIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        tint = AppTheme.colors.colorRed,
        painter = painterResource(id = R.drawable.icon_priority_high),
        contentDescription = stringResource(R.string.high_priority),
    )
}

@Composable
private fun LowPriorityIcon(modifier: Modifier = Modifier) {
    Icon(
        modifier = modifier,
        tint = AppTheme.colors.colorGrayLight,
        painter = painterResource(id = R.drawable.icon_priority_low),
        contentDescription = stringResource(R.string.low_priority)
    )
}

@Composable
private fun DescriptionText(todoItem: TodoItemUiModel) {
    val descriptionTextStyle = if (todoItem.progress == Progress.NotCompleted) {
        AppTheme.typography.body
    } else {
        AppTheme.typography.body.copy(
            color = AppTheme.colors.labelTertiary,
            textDecoration = TextDecoration.LineThrough,
        )
    }
    Text(
        modifier = Modifier.padding(4.dp),
        text = todoItem.description,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        style = descriptionTextStyle,
    )
}

private data class TodoItemPreviewParams(
    val priority: Priority,
    val progress: Progress,
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun TodoItemPreview(
    @PreviewParameter(TodoItemPreviewParamsProvider::class) params: TodoItemPreviewParams,
) {
    val todoItem = TodoItemUiModel(
        id = "",
        description = "Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. ",
        priority = params.priority,
        progress = params.progress,
        deadline = "June 24, 2024"
    )
    AppTheme {
        TodoItemComposable(todoItem = todoItem, onEvent = {})
    }
}

private class TodoItemPreviewParamsProvider : PreviewParameterProvider<TodoItemPreviewParams> {
    override val values = sequenceOf(
        TodoItemPreviewParams(
            progress = Progress.Completed,
            priority = Priority.High,
        ),
        TodoItemPreviewParams(
            progress = Progress.NotCompleted,
            priority = Priority.Low,
        ),
        TodoItemPreviewParams(
            progress = Progress.NotCompleted,
            priority = Priority.High,
        ),
    )
}
