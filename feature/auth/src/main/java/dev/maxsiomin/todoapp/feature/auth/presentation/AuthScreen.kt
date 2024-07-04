package dev.maxsiomin.todoapp.feature.auth.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import dev.maxsiomin.common.presentation.SnackbarCallback
import dev.maxsiomin.common.presentation.SnackbarInfo
import dev.maxsiomin.common.util.CollectFlow
import dev.maxsiomin.todoapp.feature.auth.R
import dev.maxsiomin.todoapp.navdestinations.Screen

@Composable
internal fun AuthScreen(navController: NavHostController, showSnackbar: SnackbarCallback) {

    val viewModel: AuthViewModel = hiltViewModel()

    val context = LocalContext.current
    val contract = remember {
        YandexAuthSdk.create(YandexAuthOptions(context)).contract
    }
    val launcher = rememberLauncherForActivityResult(contract) { result ->
        viewModel.onEvent(AuthViewModel.Event.OnAuthResult(result))
    }

    CollectFlow(viewModel.effectFlow) { event ->
        when (event) {
            AuthViewModel.Effect.GoToHomeScreen -> navController.navigate(Screen.HomeScreen)
            is AuthViewModel.Effect.LoginWithYandex -> launcher.launch(event.options)
            is AuthViewModel.Effect.ShowMessage -> showSnackbar(
                SnackbarInfo(message = event.message)
            )
        }
    }

    AuthScreenContent(onEvent = viewModel::onEvent)

}

@Composable
private fun AuthScreenContent(onEvent: (AuthViewModel.Event) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = {
            onEvent(AuthViewModel.Event.LoginClicked)
        }) {
            Text(text = stringResource(R.string.login_with_yandex))
        }
    }
}

