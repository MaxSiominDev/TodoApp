package dev.maxsiomin.todoapp.feature.todolist.data.remote.dto

import kotlinx.serialization.SerialName

data class GetTodoItemByIdResponse(
    val status: String,

    @SerialName("element")
    val item: TodoItemDto,

    val revision: Int,
)