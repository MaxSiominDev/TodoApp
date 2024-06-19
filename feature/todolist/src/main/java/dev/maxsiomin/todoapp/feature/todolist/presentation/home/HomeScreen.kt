package dev.maxsiomin.todoapp.feature.todolist.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

    HomeScreenContent(state, viewModel::onEvent)

}

@Composable
private fun HomeScreenContent(state: HomeViewModel.State, onEvent: (HomeViewModel.Event) -> Unit) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(AppTheme.colors.backPrimary)) {
        Column(Modifier.fillMaxSize()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 48.dp)
            ) {
                Spacer(modifier = Modifier.height(100.dp))
                Text(
                    text = stringResource(R.string.my_todos),
                    style = AppTheme.typography.largeTitle,
                )
                Text(
                    text = stringResource(R.string.completed, state.completedCount),
                    style = AppTheme.typography.subhead,
                )
            }

        }

        FabAdd(onEvent)
    }

}

@Composable
private fun BoxScope.FabAdd(onEvent: (HomeViewModel.Event) -> Unit, modifier: Modifier = Modifier) {
    FloatingActionButton(
        modifier = modifier
            .align(Alignment.BottomEnd)
            .padding(end = 24.dp, bottom = 60.dp),
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
    HomeScreenContent(state = HomeViewModel.State(emptyList(), "5"), onEvent = {})
}
