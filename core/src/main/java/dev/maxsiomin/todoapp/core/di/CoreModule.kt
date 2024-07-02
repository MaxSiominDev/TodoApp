package dev.maxsiomin.todoapp.core.di

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.maxsiomin.todoapp.core.data.JvmUuidGenerator
import dev.maxsiomin.todoapp.core.domain.UuidGenerator
import dev.maxsiomin.todoapp.core.util.AndroidDeviceIdManager
import dev.maxsiomin.todoapp.core.util.ApiKeys
import dev.maxsiomin.todoapp.core.util.DeviceIdManager
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.core.util.StandardDispatchers
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.AttributeKey
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface CoreModule {

    @Singleton
    @Binds
    fun bindDeviceIdGenerator(impl: AndroidDeviceIdManager): DeviceIdManager

    @Singleton
    @Binds
    fun bindUuidGenerator(impl: JvmUuidGenerator): UuidGenerator

    companion object {

        @Singleton
        @Provides
        fun provideHttpClient(): HttpClient {
            return HttpClient(Android) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
                install(DefaultRequest) {
                    header(HttpHeaders.Authorization, "Bearer ${ApiKeys.YANDEX_API_KEY}")
                    header(HttpHeaders.ContentType, "application/json")
                    header(HttpHeaders.Accept, "application/json")
                }
                install(HttpRequestRetry) {
                    retryOnServerErrors(maxRetries = 5)
                    exponentialDelay()
                }
            }
        }

        @Provides
        fun provideDispatcherProvider(): DispatcherProvider {
            return StandardDispatchers
        }

        @Provides
        fun provideSharedPrefs(@ApplicationContext context: Context): SharedPreferences {
            return PreferenceManager.getDefaultSharedPreferences(context)
        }

    }

}
