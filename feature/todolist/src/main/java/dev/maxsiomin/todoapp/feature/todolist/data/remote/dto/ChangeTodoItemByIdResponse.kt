package dev.maxsiomin.todoapp.feature.todolist.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChangeTodoItemByIdResponse(
    @SerialName("status")
    val status: String,

    @SerialName("element")
    val item: TodoItemDto,

    @SerialName("revision")
    val revision: Int,
)

