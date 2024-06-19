package dev.maxsiomin.todoapp.core.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.maxsiomin.todoapp.core.util.DateFormatter
import dev.maxsiomin.todoapp.core.util.DefaultDateFormatter
import dev.maxsiomin.todoapp.core.util.LocaleLanguage
import dev.maxsiomin.todoapp.core.util.LocaleManager
import dev.maxsiomin.todoapp.core.util.LocaleManagerImpl
import dev.maxsiomin.todoapp.core.util.RussianDateFormatter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideDateFormatter(
        localeManager: LocaleManager,
        @ApplicationContext context: Context,
    ): DateFormatter {
        return when (localeManager.getLocaleLanguage()) {
            LocaleLanguage.Default -> DefaultDateFormatter(context)
            LocaleLanguage.Ru -> RussianDateFormatter(context)
        }
    }

    @Provides
    fun provideLocaleManager(impl: LocaleManagerImpl): LocaleManager = impl

}
