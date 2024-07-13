package dev.maxsiomin.todoapp.feature.todolist.presentation.edit

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.maxsiomin.common.extensions.now
import dev.maxsiomin.common.extensions.toEpochMillis
import dev.maxsiomin.common.extensions.toLocalDate
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.common.presentation.SnackbarInfo
import dev.maxsiomin.common.util.CollectFlow
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.core.presentation.theme.PreviewConfig
import dev.maxsiomin.todoapp.core.presentation.theme.PreviewConfigProvider
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import kotlinx.datetime.LocalDate

@Composable
fun EditScreen(navController: NavHostController, showSnackbar: SnackbarCallback) {

    val viewModel: EditViewModel = hiltViewModel()

    val context = LocalContext.current
    CollectFlow(viewModel.effectFlow) { event ->
        when (event) {
            EditViewModel.Effect.GoBack -> navController.navigateUp()
            is EditViewModel.Effect.ShowMessage -> showSnackbar(
                SnackbarInfo(message = event.message)
            )

            is EditViewModel.Effect.ShowToast -> {
                Toast.makeText(context, event.message.asString(context), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    EditScreenContentWithTopAppBar(state = state, onEvent = viewModel::onEvent)

}

@Composable
private fun EditScreenContentWithTopAppBar(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit
) {

    val scrollState = rememberScrollState()
    val appBarElevation by remember {
        derivedStateOf {
            if (scrollState.value > 0) {
                4.dp
            } else {
                0.dp
            }
        }
    }
    Scaffold(
        topBar = {
            TopBar(
                onEvent = onEvent,
                background = AppTheme.colors.backPrimary,
                elevation = appBarElevation,
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backPrimary)
                .padding(padding)
        ) {
            EditScreenMainContent(state, scrollState, onEvent)
        }
    }

}

@Composable
private fun EditScreenMainContent(
    state: EditViewModel.State,
    scrollState: ScrollState,
    onEvent: (EditViewModel.Event) -> Unit
) {
    if (state.showSelectDeadlineDateDialog) {
        SelectDeadlineDialog(state, onEvent)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backPrimary)
            .verticalScroll(scrollState)
    ) {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectDeadlineDialog(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit,
) {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = state.deadlineDate.toEpochMillis())
    DatePickerDialog(
        onDismissRequest = { onEvent(EditViewModel.Event.SelectDeadlineDialogDismissed) },
        confirmButton = {
            TextButton(onClick = {
                datePickerState.selectedDateMillis?.toLocalDate()?.let { newDate ->
                    onEvent(EditViewModel.Event.NewDeadlineDateSelected(newDate))
                }
            }) {
                Text(text = stringResource(android.R.string.ok))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onEvent(EditViewModel.Event.SelectDeadlineDialogDismissed)
            }) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
private fun TopBar(onEvent: (EditViewModel.Event) -> Unit, background: Color, elevation: Dp) {
    val elevationAnimation by animateDpAsState(
        targetValue = elevation,
        label = "Top App Bar Shadow"
    )
    Surface(modifier = Modifier.shadow(elevation = elevationAnimation)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(background)
                .padding(start = 0.dp, end = 20.dp, top = 12.dp, bottom = 0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onEvent(EditViewModel.Event.CloseClicked) }) {
                Icon(
                    tint = AppTheme.colors.labelPrimary,
                    painter = painterResource(R.drawable.icon_close),
                    contentDescription = stringResource(R.string.go_back_without_saving),
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                modifier = Modifier.clickable {
                    onEvent(EditViewModel.Event.SaveClicked)
                },
                text = stringResource(R.string.save),
                style = AppTheme.typography.body.copy(color = AppTheme.colors.colorBlue)
            )
        }
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
            textStyle = AppTheme.typography.body,
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

@Composable
private fun PriorityTexts(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .clickable {
                onEvent(EditViewModel.Event.ShowPriorityBottomSheet)
            }
    ) {
        Text(text = stringResource(R.string.priority), style = AppTheme.typography.body)
        Spacer(modifier = Modifier.height(2.dp))
        val priorityTextResId = remember(state.priority) {
            when (state.priority) {
                Priority.Default -> R.string.default_
                Priority.High -> R.string.high
                Priority.Low -> R.string.low
            }
        }
        Text(text = stringResource(id = priorityTextResId), style = AppTheme.typography.subhead)
    }

    if (state.showPriorityBottomSheet) {
        PriorityBottomSheet(
            onDismissRequest = {
                onEvent(EditViewModel.Event.DismissPriorityBottomSheet)
            },
            onSelect = { newPriority ->
                onEvent(EditViewModel.Event.NewPrioritySelected(newPriority = newPriority))
            }
        )
    }

}

/**@Composable
private fun SelectPriorityDropdown(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit
) {
    DropdownMenu(
        modifier = Modifier.background(AppTheme.colors.backSecondary),
        expanded = state.priorityDropdownExpanded,
        onDismissRequest = { onEvent(EditViewModel.Event.CollapsePriorityDropdown) }
    ) {
        DropdownMenuItem(
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.labelPrimary,
            ),
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
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.labelPrimary,
            ),
            text = { Text(stringResource(R.string.default_)) },
            onClick = { onEvent(EditViewModel.Event.NewPrioritySelected(Priority.Default)) },
            leadingIcon = { Spacer(modifier = Modifier.width(24.dp)) }
        )
        DropdownMenuItem(
            colors = MenuDefaults.itemColors(
                textColor = AppTheme.colors.colorRed,
            ),
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
}*/

@Composable
private fun DeadlineRow(
    state: EditViewModel.State,
    onEvent: (EditViewModel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = stringResource(R.string.complete_before), style = AppTheme.typography.body)
            Spacer(modifier = Modifier.height(4.dp))

            AnimatedVisibility(state.deadLineSwitchIsOn) {
                Text(
                    modifier = Modifier.clickable {
                        onEvent(EditViewModel.Event.SelectDeadlineDateClicked)
                    },
                    text = state.deadlineStringDate,
                    style = AppTheme.typography.subhead.copy(color = AppTheme.colors.colorBlue)
                )
            }
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
    Button(
        modifier = modifier,
        onClick = { onEvent(EditViewModel.Event.DeleteIconClicked) },
        colors = ButtonDefaults.textButtonColors(
            contentColor = Color.Transparent,
        ),
    ) {
        Icon(
            tint = AppTheme.colors.colorRed,
            painter = painterResource(R.drawable.icon_delete),
            contentDescription = stringResource(R.string.delete)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(R.string.delete),
            style = AppTheme.typography.body.copy(color = AppTheme.colors.colorRed)
        )
    }
}

@Preview
@Composable
private fun EditScreenPreview(
    @PreviewParameter(PreviewConfigProvider::class) config: PreviewConfig,
) {
    AppTheme(isDarkTheme = config.isDarkTheme) {
        EditScreenContentWithTopAppBar(
            state = EditViewModel.State(
                deadlineDate = LocalDate.now(),
                deadlineStringDate = "June 2, 2025",
                deadLineSwitchIsOn = true,
                description = "",
                priority = Priority.Default,
            ), onEvent = {}
        )
    }
}
