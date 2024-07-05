package dev.maxsiomin.todoapp.feature.todolist.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

/** Represents todo item when communicating to the API */
@Serializable
internal data class TodoItemDto(
    @SerialName("id")
    val id: String,

    @SerialName("text")
    val description: String,

    @SerialName("importance")
    val importance: String,

    @SerialName("deadline")
    val deadline: Long? = null,

    @SerialName("done")
    val isCompleted: Boolean,

    @SerialName("color")
    val color: String? = null,

    @SerialName("created_at")
    val createdAt: Long,

    @SerialName("changed_at")
    val changedAt: Long,

    @SerialName("last_updated_by")
    val lastUpdatedBy: String,
)