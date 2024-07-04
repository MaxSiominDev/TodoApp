package dev.maxsiomin.todoapp.feature.todolist.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class UpdateTodoItemsListResponse(
    @SerialName("status")
    val status: String,

    @SerialName("list")
    val items: List<TodoItemDto>,

    @SerialName("revision")
    val revision: Int,
)
