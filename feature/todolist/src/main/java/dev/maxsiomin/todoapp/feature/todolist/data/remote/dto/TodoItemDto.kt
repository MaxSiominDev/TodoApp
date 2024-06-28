package dev.maxsiomin.todoapp.feature.todolist.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemDto(
    val id: String,

    @SerialName("text")
    val description: String,

    val importance: String,

    val deadline: Long? = null,

    @SerialName("done")
    val isCompleted: Boolean,

    val color: String? = null,

    @SerialName("created_at")
    val createdAt: Long,

    @SerialName("changed_at")
    val changedAt: Long,

    @SerialName("last_updated_by")
    val lastUpdatedBy: String,
)