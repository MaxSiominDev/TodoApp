package dev.maxsiomin.todoapp.core.data.ktor

import dev.maxsiomin.common.domain.resource.NetworkError
import dev.maxsiomin.common.domain.resource.Resource
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.put

suspend inline fun <reified T> HttpClient.safePut(
    requestBuilder: HttpRequestBuilder.() -> Unit
): Resource<T, NetworkError> {
    return safeRequest {
        put { requestBuilder() }
    }
}
