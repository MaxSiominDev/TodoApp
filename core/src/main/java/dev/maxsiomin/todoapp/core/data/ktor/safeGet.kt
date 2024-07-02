package dev.maxsiomin.todoapp.core.data.ktor

import dev.maxsiomin.common.domain.resource.NetworkError
import dev.maxsiomin.common.domain.resource.Resource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> HttpClient.safeGet(
    requestBuilder: HttpRequestBuilder.() -> Unit
): Resource<T, NetworkError> {
    return safeRequest {
        get { requestBuilder() }
    }
}
