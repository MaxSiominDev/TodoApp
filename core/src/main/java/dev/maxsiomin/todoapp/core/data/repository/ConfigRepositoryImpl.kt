package dev.maxsiomin.todoapp.core.data.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import dev.maxsiomin.todoapp.core.data.PrefsKeys
import dev.maxsiomin.todoapp.core.domain.Theme
import dev.maxsiomin.todoapp.core.domain.repository.ConfigRepository
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@SuppressLint("ApplySharedPref")
internal class ConfigRepositoryImpl @Inject constructor(
    private val prefs: SharedPreferences,
    private val dispatchers: DispatcherProvider,
) : ConfigRepository {

    private var themeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    private val themeFlow by lazy {
        callbackFlow {
            fun update() {
                val strValue = prefs.getString(PrefsKeys.THEME, null)
                    ?: Theme.SystemDefault.toString()
                trySend(Theme.valueOf(strValue))
            }
            themeListener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (key != PrefsKeys.THEME) return@OnSharedPreferenceChangeListener
                update()
            }.also { listener ->
                prefs.registerOnSharedPreferenceChangeListener(listener)
            }

            update()

            awaitClose { prefs.unregisterOnSharedPreferenceChangeListener(themeListener) }
        }.flowOn(dispatchers.io)
    }

    override fun setToken(token: String) {
        prefs.edit().putString(PrefsKeys.TOKEN, token).commit()
    }

    override fun getToken(): String? {
        return prefs.getString(PrefsKeys.TOKEN, null)
    }

    override fun saveTheme(theme: Theme) {
        val strValue = theme.toString()
        prefs.edit().putString(PrefsKeys.THEME, strValue).commit()
    }

    override fun getTheme(): Theme {
        val strValue = prefs.getString(PrefsKeys.THEME, null)
            ?: Theme.SystemDefault.toString()
        return Theme.valueOf(strValue)
    }

    override suspend fun getThemeFlow(): Flow<Theme> = withContext(dispatchers.io) {
        return@withContext themeFlow
    }

}
