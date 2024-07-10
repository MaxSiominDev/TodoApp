package dev.maxsiomin.todoapp.core.domain.repository

interface TokenRepository {

    fun setToken(token: String)

    fun getToken(): String?

}