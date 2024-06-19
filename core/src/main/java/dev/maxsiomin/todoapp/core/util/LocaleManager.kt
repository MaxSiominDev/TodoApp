package dev.maxsiomin.todoapp.core.util

import java.util.Locale
import javax.inject.Inject

interface LocaleManager {
    fun getLocaleLanguage(): LocaleLanguage

    companion object {
        const val DEFAULT_LOCALE = "en"
    }
}

sealed class LocaleLanguage(val value: String) {
    data object Default : LocaleLanguage("en")
    data object Ru : LocaleLanguage("ru")
}

class LocaleManagerImpl @Inject constructor() : LocaleManager {

    private val currentLocale = Locale.getDefault()

    private val localeLanguage = currentLocale.language

    override fun getLocaleLanguage(): LocaleLanguage {
        return when (localeLanguage) {
            LocaleLanguage.Default.value -> LocaleLanguage.Default
            LocaleLanguage.Ru.value -> LocaleLanguage.Ru
            else -> LocaleLanguage.Default
        }
    }

}
