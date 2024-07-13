package dev.maxsiomin.todoapp.core.domain.repository

import dev.maxsiomin.todoapp.core.domain.Theme
import kotlinx.coroutines.flow.Flow

interface ConfigRepository {

    fun setToken(token: String)

    fun getToken(): String?

    fun saveTheme(theme: Theme)

    fun getTheme(): Theme

    suspend fun getThemeFlow(): Flow<Theme>

}