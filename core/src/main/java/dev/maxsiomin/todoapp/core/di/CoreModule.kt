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
import dev.maxsiomin.todoapp.core.data.ConnectivityObserver
import dev.maxsiomin.todoapp.core.data.JvmUuidGenerator
import dev.maxsiomin.todoapp.core.data.AndroidConnectivityObserver
import dev.maxsiomin.todoapp.core.data.PrefsKeys
import dev.maxsiomin.todoapp.core.data.repository.TokenRepositoryImpl
import dev.maxsiomin.todoapp.core.domain.UuidGenerator
import dev.maxsiomin.todoapp.core.domain.repository.TokenRepository
import dev.maxsiomin.todoapp.core.util.AndroidDeviceIdManager
import dev.maxsiomin.todoapp.core.util.DeviceIdManager
import dev.maxsiomin.todoapp.core.util.DispatcherProvider
import dev.maxsiomin.todoapp.core.util.StandardDispatchers
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.DefaultRequest
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
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

    @Binds
    fun bindConnectivityObserver(impl: AndroidConnectivityObserver): ConnectivityObserver

    @Singleton
    @Binds
    fun bindTokenRepository(impl: TokenRepositoryImpl): TokenRepository

    companion object {

        @Singleton
        @Provides
        fun provideHttpClient(repository: TokenRepository): HttpClient {
            val token: String? = repository.getToken()
            return HttpClient(Android) {
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
                install(DefaultRequest) {
                    header(HttpHeaders.Authorization, "OAuth $token")
                    header(HttpHeaders.ContentType, ContentType.Application.Json)
                    header(HttpHeaders.Accept, ContentType.Application.Json)
                }
                install(HttpRequestRetry) {
                    retryOnServerErrors(maxRetries = 2)
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
