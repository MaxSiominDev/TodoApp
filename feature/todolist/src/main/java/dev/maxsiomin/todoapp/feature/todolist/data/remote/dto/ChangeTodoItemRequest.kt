package dev.maxsiomin.todoapp.feature.todolist.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ChangeTodoItemRequest(
    @SerialName("status")
    val status: String,

    @SerialName("element")
    val item: TodoItemDto,
)