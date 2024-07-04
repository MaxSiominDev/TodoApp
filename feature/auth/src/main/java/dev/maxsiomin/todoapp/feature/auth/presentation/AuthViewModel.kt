package dev.maxsiomin.todoapp.feature.auth.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import com.yandex.authsdk.YandexAuthLoginOptions
import com.yandex.authsdk.YandexAuthOptions
import com.yandex.authsdk.YandexAuthSdk
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(@ApplicationContext context: Context) : ViewModel() {

    val sdk = YandexAuthSdk.create(YandexAuthOptions(context))
    val loginOptions = YandexAuthLoginOptions().copy()

}