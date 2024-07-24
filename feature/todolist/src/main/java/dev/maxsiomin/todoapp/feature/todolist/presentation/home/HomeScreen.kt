package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.maxsiomin.common.extensions.now
import dev.maxsiomin.common.extensions.toLocalizedDate
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.common.presentation.SnackbarInfo
import dev.maxsiomin.common.presentation.UiText
import dev.maxsiomin.common.util.CollectFlow
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.core.presentation.theme.PreviewConfig
import dev.maxsiomin.todoapp.core.presentation.theme.PreviewConfigProvider
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import kotlinx.datetime.LocalDate

@Composable
internal fun HomeScreen(
    goToEditScreen: (String?) -> Unit,
    goToSettingsScreen: () -> Unit,
    showSnackbar: SnackbarCallback,
) {

    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectFlow(viewModel.effectFlow) { event ->
        when (event) {
            is HomeViewModel.Effect.GoToEditScreen -> goToEditScreen(event.itemId)

            is HomeViewModel.Effect.ShowMessage -> showSnackbar(
                SnackbarInfo(message = event.message)
            )

            is HomeViewModel.Effect.OnItemDeletedMessage -> {
                showSnackbar(
                    SnackbarInfo(
                        message = event.name,
                        action = UiText.StringResource(R.string.undo),
                        duration = SnackbarDuration.Long,
                        dismissPreviousSnackbarImmediately = true,
                        onResult = { result ->
                            when (result) {
                                SnackbarResult.Dismissed -> viewModel.onEvent(
                                    HomeViewModel.Event.FinallyDelete(event.id)
                                )
                                SnackbarResult.ActionPerformed -> viewModel.onEvent(
                                    HomeViewModel.Event.CancelDeletion(event.id)
                                )
                            }
                        }
                    )
                )
            }

            HomeViewModel.Effect.GoToSettings -> goToSettingsScreen()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeViewModel.Event.Refresh)
    }

    HomeScreenContentWithTopAppBar(state, viewModel::onEvent)

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreenContentWithTopAppBar(
    state: HomeViewModel.State,
    onEvent: (HomeViewModel.Event) -> Unit
) {
    val listState = rememberLazyListState()
    val expanded by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex == 0 && listState.firstVisibleItemScrollOffset == 0
        }
    }
    val elevation by remember {
        derivedStateOf {
            if (expanded) 0.dp else 4.dp
        }
    }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = { FabAdd(onEvent = onEvent) },
        topBar = {
            TopBar(
                state = state,
                expanded = expanded,
                elevation = elevation,
                scrollBehavior = scrollBehavior,
                onEvent = onEvent,
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppTheme.colors.backPrimary)
                .padding(top = padding.calculateTopPadding())
        ) {
            HomeScreenMainContent(state, listState, onEvent)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    state: HomeViewModel.State,
    expanded: Boolean,
    elevation: Dp,
    scrollBehavior: TopAppBarScrollBehavior,
    onEvent: (HomeViewModel.Event) -> Unit,
) {
    val elevationAnimation by animateDpAsState(
        targetValue = elevation,
        label = "Top App Bar Shadow"
    )
    Surface(modifier = Modifier.shadow(elevation = elevationAnimation)) {
        LargeTopAppBar(
            modifier = Modifier,
            title = {
                TopBarContent(
                    state = state,
                    expanded = expanded,
                    onEvent = onEvent,
                )
            },
            scrollBehavior = scrollBehavior,
            colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = AppTheme.colors.backPrimary,
                scrolledContainerColor = AppTheme.colors.backPrimary,
            )
        )
    }
}

@Composable
private fun TopBarContent(
    state: HomeViewModel.State,
    expanded: Boolean,
    onEvent: (HomeViewModel.Event) -> Unit
) {
    Row(
        modifier = Modifier
            .background(AppTheme.colors.backPrimary)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.padding(start = 48.dp)) {
            Text(
                text = stringResource(R.string.my_todos),
                style = AppTheme.typography.largeTitle
            )
            AnimatedVisibility(visible = expanded) {
                Text(
                    text = stringResource(R.string.completed, state.completedCount),
                    style = AppTheme.typography.body.copy(
                        color = AppTheme.colors.labelTertiary,
                    ),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        IconRetry(onEvent = onEvent)
        IconHideCompleted(state, onEvent)
        IconSettings(onEvent = onEvent, Modifier.padding(end = 16.dp))
    }
}

@Composable
private fun HomeScreenMainContent(
    state: HomeViewModel.State,
    listState: LazyListState,
    onEvent: (HomeViewModel.Event) -> Unit
) {

    LazyColumn(
        state = listState,
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(AppTheme.colors.backSecondary)
    ) {
        items(
            items = state.todoItems,
            key = {
                it.id
            },
        ) {
            TodoItemComposable(
                todoItem = it,
                onEvent = onEvent,
                modifier = Modifier.background(AppTheme.colors.backSecondary),
            )
        }

        if (state.todoItems.isNotEmpty()) {
            item {
                ButtonNew(onEvent, Modifier.padding(start = 36.dp, bottom = 8.dp))
            }
        }
    }

}

@Composable
private fun ButtonNew(onEvent: (HomeViewModel.Event) -> Unit, modifier: Modifier = Modifier) {
    TextButton(
        modifier = modifier,
        onClick = { onEvent(HomeViewModel.Event.AddClicked) },
    ) {
        Text(
            text = stringResource(R.string.new_),
            style = AppTheme.typography.body.copy(color = AppTheme.colors.labelTertiary),
        )
    }
}

@Composable
private fun IconRetry(onEvent: (HomeViewModel.Event) -> Unit, modifier: Modifier = Modifier) {
    IconButton(
        modifier = modifier,
        onClick = { onEvent(HomeViewModel.Event.Refresh) }
    ) {
        Icon(
            tint = AppTheme.colors.colorBlue,
            imageVector = Icons.Filled.Refresh,
            contentDescription = stringResource(R.string.retry)
        )
    }
}

@Composable
private fun IconHideCompleted(
    state: HomeViewModel.State,
    onEvent: (HomeViewModel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = { onEvent(HomeViewModel.Event.IconHideCompletedClicked) }
    ) {
        val (painterRes, contentDescriptionRes) = if (state.hideCompleted) {
            R.drawable.icon_show to R.string.show_completed
        } else {
            R.drawable.icon_hide to R.string.hide_completed
        }
        Icon(
            tint = AppTheme.colors.colorBlue,
            painter = painterResource(painterRes),
            contentDescription = stringResource(contentDescriptionRes)
        )
    }
}

@Composable
private fun IconSettings(
    onEvent: (HomeViewModel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = { onEvent(HomeViewModel.Event.OnSettingsClicked) }
    ) {
        Icon(
            tint = AppTheme.colors.colorBlue,
            imageVector = Icons.Filled.Settings,
            contentDescription = stringResource(R.string.settings)
        )
    }
}

@Composable
private fun FabAdd(onEvent: (HomeViewModel.Event) -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        modifier = modifier,
        onClick = {
            onEvent(HomeViewModel.Event.AddClicked)
        },
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_todo),
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(PreviewConfigProvider::class) config: PreviewConfig,
) {
    val items = mutableListOf(
        TodoItemUiModel(
            id = "",
            description = "My todo item",
            priority = Priority.High,
            isCompleted = true,
            deadline = LocalDate.now().toLocalizedDate(),
        )
    )
    AppTheme(isDarkTheme = config.isDarkTheme) {
        HomeScreenContentWithTopAppBar(
            state = HomeViewModel.State(items, "5"),
            onEvent = {}
        )
    }
}
