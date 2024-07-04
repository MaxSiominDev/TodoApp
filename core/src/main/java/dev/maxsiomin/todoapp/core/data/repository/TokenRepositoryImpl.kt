package dev.maxsiomin.todoapp.core.data.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import dev.maxsiomin.todoapp.core.data.PrefsKeys
import dev.maxsiomin.todoapp.core.domain.repository.TokenRepository
import javax.inject.Inject

internal class TokenRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences,
) : TokenRepository {

    @SuppressLint("ApplySharedPref")
    override fun setToken(token: String) {
        prefs.edit().putString(PrefsKeys.TOKEN, token).commit()
    }

    override fun getToken(): String? {
        return prefs.getString(PrefsKeys.TOKEN, null)
    }

}
