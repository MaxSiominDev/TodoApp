package dev.maxsiomin.todoapp.core.data.ktor

import dev.maxsiomin.common.domain.resource.NetworkError
import dev.maxsiomin.common.domain.resource.Resource
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete

suspend inline fun <reified T> HttpClient.safeDelete(
    requestBuilder: HttpRequestBuilder.() -> Unit
): Resource<T, NetworkError> {
    return safeRequest {
        delete { requestBuilder() }
    }
}
