package dev.maxsiomin.todoapp.feature.todolist.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.maxsiomin.common.extensions.now
import dev.maxsiomin.common.presentation.components.DatePickerDialog
import dev.maxsiomin.common.util.CollectFlow
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import kotlinx.datetime.LocalDate

@Composable
fun EditScreen(navController: NavHostController) {

    val viewModel: EditViewModel = hiltViewModel()

    CollectFlow(viewModel.effectFlow) { event ->
        when (event) {
            EditViewModel.Effect.GoBack -> navController.navigateUp()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    EditScreenContent(state = state, onEvent = viewModel::onEvent)

}

@Composable
private fun EditScreenContent(state: EditViewModel.State, onEvent: (EditViewModel.Event) -> Unit) {

    if (state.showSelectDeadlineDateDialog) {
        DatePickerDialog(
            date = state.deadlineDate,
            onDateChange = { onEvent(EditViewModel.Event.NewDeadlineDateSelected(it)) }
        )
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backPrimary)
    ) {
        TopBar(onEvent)
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.verticalScroll(scrollState)) {
            Spacer(modifier = Modifier.height(32.dp))
            DescriptionTextField(state, onEvent)
            PriorityTexts(state, onEvent)
            Spacer(modifier = Modifier.height(20.dp))
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(AppTheme.colors.supportSeparator)
            )
            DeadlineRow(state, onEvent)
            Spacer(modifier = Modifier.height(28.dp))
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .fillMaxWidth()
                    .background(AppTheme.colors.supportSeparator)
            )
            Spacer(modifier = Modifier.height(12.dp))
            DeleteTextAndIcon(onEvent)
        }
    }

}

@Composable
private fun TopBar(onEvent: (EditViewModel.Event) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_close),
            contentDescription = stringResource(R.string.go_back_without_saving),
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            modifier = Modifier.clickable {
                onEvent(EditViewModel.Event.SaveClicked)
            },
            text = stringResource(R.string.save),
            style = AppTheme.typography.subhead.copy(color = AppTheme.colors.colorBlue)
        )
    }
}

@Composable
private fun DescriptionTextField(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(8.dp)
    Surface(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        shadowElevation = 2.dp,
        shape = shape,
    ) {
        TextField(
            modifier = modifier
                .fillMaxWidth(),
            value = state.description,
            onValueChange = {
                onEvent(EditViewModel.Event.OnDescriptionChanged(it))
            },
            placeholder = {
                Text(text = stringResource(R.string.what_to_do))
            },
            shape = shape,
            colors = TextFieldDefaults.colors(
                focusedTextColor = AppTheme.colors.labelPrimary,
                unfocusedTextColor = AppTheme.colors.labelPrimary,
                focusedPlaceholderColor = AppTheme.colors.labelTertiary,
                unfocusedPlaceholderColor = AppTheme.colors.labelTertiary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                focusedContainerColor = AppTheme.colors.backSecondary,
                unfocusedContainerColor = AppTheme.colors.backSecondary
            ),
            minLines = 5,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PriorityTexts(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onEvent(EditViewModel.Event.ExpandPriorityDropdown)
            }
    ) {
        Text(text = stringResource(R.string.priority), style = AppTheme.typography.body)
        Spacer(modifier = Modifier.height(2.dp))
        val priorityTextResId = remember(state.priority) {
            when (state.priority) {
                Priority.Default -> R.string.default_priority
                Priority.High -> R.string.high_priority
                Priority.Low -> R.string.low_priority
            }
        }
        Text(text = stringResource(id = priorityTextResId), style = AppTheme.typography.subhead)
        SelectPriorityDropdown(state, onEvent)
    }

}

@Composable
private fun SelectPriorityDropdown(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit
) {
    DropdownMenu(
        expanded = state.priorityDropdownExpanded,
        onDismissRequest = { onEvent(EditViewModel.Event.CollapsePriorityDropdown) }
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.low)) },
            onClick = { onEvent(EditViewModel.Event.NewPrioritySelected(Priority.Low)) },
            leadingIcon = {
                Icon(
                    tint = AppTheme.colors.colorGray,
                    painter = painterResource(R.drawable.icon_priority_low),
                    contentDescription = null,
                )
            }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.default_)) },
            onClick = { onEvent(EditViewModel.Event.NewPrioritySelected(Priority.Default)) },
            leadingIcon = { Spacer(modifier = Modifier.width(24.dp)) }
        )
        DropdownMenuItem(
            text = { Text(stringResource(R.string.high)) },
            onClick = { onEvent(EditViewModel.Event.NewPrioritySelected(Priority.High)) },
            leadingIcon = {
                Icon(
                    tint = AppTheme.colors.colorRed,
                    painter = painterResource(R.drawable.icon_priority_high),
                    contentDescription = stringResource(R.string.high_priority),
                )
            },
        )
    }
}

@Composable
private fun DeadlineRow(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = stringResource(R.string.complete_before), style = AppTheme.typography.body)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                modifier = Modifier.clickable {
                    onEvent(EditViewModel.Event.SelectDeadlineDateClicked)
                },
                text = state.deadlineStringDate,
                style = AppTheme.typography.subhead.copy(color = AppTheme.colors.colorBlue)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = state.deadLineSwitchIsOn,
            onCheckedChange = {
                onEvent(EditViewModel.Event.DeadlineSwitchChecked(it))
            }
        )
    }
}

@Composable
private fun DeleteTextAndIcon(
    onEvent: (EditViewModel.Event) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                onEvent(EditViewModel.Event.DeleteIconClicked)
            }
        ) {
            Icon(
                tint = AppTheme.colors.colorRed,
                painter = painterResource(R.drawable.icon_delete),
                contentDescription = stringResource(R.string.delete)
            )
        }
        Text(
            text = stringResource(R.string.delete),
            style = AppTheme.typography.body.copy(color = AppTheme.colors.colorRed)
        )
    }
}

@Preview
@Composable
private fun EditScreenPreview() {
    EditScreenContent(
        state = EditViewModel.State(
            deadlineDate = LocalDate.now(),
            deadlineStringDate = "June 2, 2025",
            deadLineSwitchIsOn = true,
            description = "",
            priority = Priority.Default,
        ), onEvent = {}
    )
}
