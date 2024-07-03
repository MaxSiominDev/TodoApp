package dev.maxsiomin.todoapp.feature.auth.presentation

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.yandex.authsdk.YandexAuthResult

@Composable
fun AuthScreen() {

    val viewModel: AuthViewModel = hiltViewModel()

    val launcher = rememberLauncherForActivityResult(viewModel.sdk.contract) { result ->
        when (result) {
            YandexAuthResult.Cancelled -> Unit
            is YandexAuthResult.Failure -> {
                println(result)
            }
            is YandexAuthResult.Success -> {
                println(result)
            }
        }
    }

    Button(onClick = {
        launcher.launch(viewModel.loginOptions)
    }) {
        Text(text = "Login with Yandex")
    }

}
