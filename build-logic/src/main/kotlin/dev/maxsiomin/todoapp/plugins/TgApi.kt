package dev.maxsiomin.todoapp.plugins

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.escapeIfNeeded
import java.io.File

class TgApi(private val httpClient: HttpClient) {

    suspend fun sendFile(file: File, filename: String, token: String, chatId: String): HttpResponse {
        val body = MultiPartFormDataContent(
            formData {
                append("document", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentDisposition, "filename=${filename.escapeIfNeeded()}")
                })
            }
        )
        return httpClient.post {
            url("$BASE_URL/bot$token/sendDocument")
            parameter("chat_id", chatId)
            setBody(body)
        }
    }

    suspend fun sendMessage(message: String, token: String, chatId: String): HttpResponse {
        return httpClient.post("$BASE_URL/bot$token/sendMessage") {
            parameter("chat_id", chatId)
            parameter("text", message)
        }
    }

    companion object {
        const val BASE_URL = "https://api.telegram.org"
    }

}
