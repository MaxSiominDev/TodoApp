package dev.maxsiomin.todoapp.feature.todolist.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpdateTodoItemsListRequest(
    val status: String,

    @SerialName("list")
    val items: List<TodoItemDto>,
)