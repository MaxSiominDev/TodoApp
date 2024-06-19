package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import dev.maxsiomin.common.extensions.now
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Progress
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.plus

@Composable
internal fun TodoItemComposable(
    todoItem: TodoItem,
    onEvent: (HomeViewModel.Event) -> Unit,
    modifier: Modifier = Modifier
) {

    Row(modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        val isChecked = when (todoItem.progress) {
            Progress.Completed -> true
            Progress.NotCompleted -> false
        }
        val uncheckedColor = if (todoItem.priority == Priority.High) {
            MaterialTheme.colorScheme.error
        } else {
            MaterialTheme.colorScheme.onSecondary
        }
        Checkbox(
            checked = isChecked,
            onCheckedChange = {
                onEvent(
                    HomeViewModel.Event.CheckboxValueChanged(it)
                )
            },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.secondary,
                uncheckedColor = uncheckedColor
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

        Text(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f),
            text = todoItem.description,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
        )

        Icon(
            painter = painterResource(id = R.drawable.icon_info),
            contentDescription = stringResource(id = R.string.info),
        )
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

private data class TodoItemPreviewParams(
    val priority: Priority,
    val progress: Progress,
)

@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL)
@Composable
private fun TodoItemPreview(
    @PreviewParameter(TodoItemPreviewParamsProvider::class) params: TodoItemPreviewParams,
) {
    val todoItem = TodoItem(
        id = "",
        description = "Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. Pass internship interview. ",
        priority = params.priority,
        progress = params.progress,
        created = LocalDate.now(),
        modified = null,
        deadline = LocalDate.now().plus(5, DateTimeUnit.DAY)
    )
    TodoItemComposable(todoItem = todoItem, onEvent = {})
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
