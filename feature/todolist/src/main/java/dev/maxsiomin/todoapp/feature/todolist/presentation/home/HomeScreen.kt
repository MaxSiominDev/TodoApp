package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.common.util.CollectFlow
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.todolist.R
import dev.maxsiomin.todoapp.navdestinations.Screen

@Composable
internal fun HomeScreen(navController: NavHostController, showSnackbar: SnackbarCallback) {

    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    CollectFlow(viewModel.effectFlow) { event ->
        when (event) {
            is HomeViewModel.Effect.GoToEditScreen -> navController.navigate(
                Screen.EditScreen(
                    itemId = event.itemId
                )
            )

            is HomeViewModel.Effect.ShowMessage -> TODO()
        }
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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    TopBar(state = state, expanded = expanded, onEvent = onEvent)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = AppTheme.colors.backPrimary,
                    scrolledContainerColor = AppTheme.colors.backPrimary,
                )
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

@Composable
private fun TopBar(
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
                    style = AppTheme.typography.body.copy(color = AppTheme.colors.labelTertiary),
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        IconHideActive(state, onEvent, Modifier.padding(end = 16.dp))
    }
}

@Composable
private fun HomeScreenMainContent(
    state: HomeViewModel.State,
    listState: LazyListState,
    onEvent: (HomeViewModel.Event) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.backPrimary)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(AppTheme.colors.backSecondary)
        ) {
            item {
                Text(state.randomId)
            }
            items(state.todoItems) {
                TodoItemComposable(
                    todoItem = it,
                    onEvent = onEvent,
                    modifier = Modifier.background(Color.White),
                )
            }
        }

        FabAdd(onEvent, Modifier.padding(end = 24.dp, bottom = 36.dp))
    }

}

@Composable
private fun IconHideActive(
    state: HomeViewModel.State,
    onEvent: (HomeViewModel.Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier,
        onClick = { onEvent(HomeViewModel.Event.IconHideActiveClicked) }
    ) {
        val (painterRes, contentDescriptionRes) = if (state.hideActive) {
            R.drawable.icon_show to R.string.show_active
        } else {
            R.drawable.icon_hide to R.string.hide_active
        }
        Icon(
            tint = AppTheme.colors.colorBlue,
            painter = painterResource(painterRes),
            contentDescription = stringResource(contentDescriptionRes)
        )
    }
}

@Composable
private fun BoxScope.FabAdd(onEvent: (HomeViewModel.Event) -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        modifier = modifier
            .align(Alignment.BottomEnd),
        onClick = {
            onEvent(HomeViewModel.Event.AddClicked)
        },
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add),
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    val items = emptyList<TodoItemUiModel>()
    HomeScreenContentWithTopAppBar(state = HomeViewModel.State(items, "5"), onEvent = {})
}
