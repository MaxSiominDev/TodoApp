package dev.maxsiomin.todoapp.core.data

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
    return try {
        val response = this.get { requestBuilder() }
        val body: T? = response.body()
        if (body == null) {
            val error = when (response.status.value) {
                400 -> NetworkError.InvalidRequest
                401 -> NetworkError.Unauthorized
                404 -> NetworkError.NotFound
                500 -> NetworkError.Server
                else -> NetworkError.Unknown
            }
            Resource.Error(error)
        } else {
            Resource.Success(response.body())
        }
    } catch (e: CancellationException) {
        throw e
    } catch (e: RedirectResponseException) {
        Resource.Error(NetworkError.Redirected)
    } catch (e: ClientRequestException) {
        Resource.Error(NetworkError.InvalidRequest)
    } catch (e: ServerResponseException) {
        Resource.Error(NetworkError.Server)
    } catch (e: Exception) {
        Resource.Error(NetworkError.Unknown)
    }
}
