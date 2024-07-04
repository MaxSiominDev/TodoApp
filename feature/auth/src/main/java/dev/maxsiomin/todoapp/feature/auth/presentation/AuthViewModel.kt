package dev.maxsiomin.todoapp.feature.auth.presentation

import android.content.Context
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthResult
import com.yandex.authsdk.YandexAuthSdk
import com.yandex.authsdk.YandexAuthSdkContract
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.maxsiomin.common.presentation.StatefulViewModel
import dev.maxsiomin.common.presentation.UiText
import dev.maxsiomin.todoapp.core.domain.usecase.SetTokenUseCase
import dev.maxsiomin.todoapp.feature.auth.R
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    private val setTokenUseCase: SetTokenUseCase,
) : StatefulViewModel<Unit, AuthViewModel.Effect, AuthViewModel.Event>() {

    override val _state = MutableStateFlow(Unit)

    sealed class Effect {
        data class LoginWithYandex(val options: YandexAuthLoginOptions) : Effect()
        data class ShowMessage(val message: UiText) : Effect()
        data object GoToHomeScreen : Effect()
    }

    sealed class Event {
        data object LoginClicked : Event()
        data class OnAuthResult(val result: YandexAuthResult) : Event()
    }

    override fun onEvent(event: Event) {
        when (event) {
            is Event.OnAuthResult -> processAuthResult(event.result)
            Event.LoginClicked -> onLoginClicked()
        }
    }

    private fun onLoginClicked() {
        val options = YandexAuthLoginOptions()
        onEffect(Effect.LoginWithYandex(options = options))
    }

    private fun processAuthResult(result: YandexAuthResult) {
        when (result) {
            YandexAuthResult.Cancelled -> Unit
            is YandexAuthResult.Failure -> {

                val message = result.exception.localizedMessage?.let {
                    UiText.DynamicString(it)
                } ?: UiText.StringResource(R.string.auth_error)
                onEffect(Effect.ShowMessage(message))
            }
            is YandexAuthResult.Success -> {
                setTokenUseCase(result.token.value)
                onEffect(Effect.GoToHomeScreen)
            }
        }
    }

}