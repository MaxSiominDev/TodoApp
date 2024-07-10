package dev.maxsiomin.todoapp.feature.todolist.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import kotlinx.datetime.LocalDate

/** Represents a todo item in the database*/
@Entity(tableName = "todoItems")
internal data class TodoItemEntity(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("priority")
    val priority: Priority,

    @ColumnInfo("isCompleted")
    val isCompleted: Boolean,

    @ColumnInfo("created")
    val created: Long,

    @ColumnInfo("modified")
    val modified: Long,

    @ColumnInfo("deadline")
    val deadline: LocalDate?,

    @ColumnInfo("lastUpdatedBy")
    val lastUpdatedBy: String,

)
