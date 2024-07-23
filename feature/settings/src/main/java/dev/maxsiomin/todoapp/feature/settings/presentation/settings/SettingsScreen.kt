package dev.maxsiomin.todoapp.feature.settings.presentation.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import dev.maxsiomin.common.util.CollectFlow
import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.presentation.components.ChooserRow
import dev.maxsiomin.todoapp.core.presentation.theme.AppTheme
import dev.maxsiomin.todoapp.feature.settings.R

@Composable
internal fun SettingsScreen(navController: NavHostController) {

    val viewModel: SettingsViewModel = hiltViewModel()

    CollectFlow(viewModel.effectFlow) { event ->
        when (event) {
            SettingsViewModel.Effect.GoBack -> navController.navigateUp()
        }
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    SettingsScreenContent(state = state, onEvent = viewModel::onEvent)

}

@Composable
private fun SettingsScreenContent(
    state: SettingsViewModel.State,
    onEvent: (SettingsViewModel.Event) -> Unit,
) {
    Surface(modifier = Modifier.fillMaxSize(), color = AppTheme.colors.backPrimary) {
        ThemeSelectionColumn(state = state, onEvent = onEvent)
    }
}

@Composable
private fun ThemeSelectionColumn(
    state: SettingsViewModel.State,
    onEvent: (SettingsViewModel.Event) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .selectableGroup(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Text(text = stringResource(R.string.dark_mode_preference), style = AppTheme.typography.title)

        ChooserRow(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.system_default),
            selected = state.selectedTheme == Theme.SystemDefault,
            onClick = {
                onEvent(SettingsViewModel.Event.ThemeChanged(newTheme = Theme.SystemDefault))
            }
        )

        ChooserRow(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.dark),
            selected = state.selectedTheme == Theme.Dark,
            onClick = {
                onEvent(SettingsViewModel.Event.ThemeChanged(newTheme = Theme.Dark))
            }
        )

        ChooserRow(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.light),
            selected = state.selectedTheme == Theme.Light,
            onClick = {
                onEvent(SettingsViewModel.Event.ThemeChanged(newTheme = Theme.Light))
            }
        )

    }

}
