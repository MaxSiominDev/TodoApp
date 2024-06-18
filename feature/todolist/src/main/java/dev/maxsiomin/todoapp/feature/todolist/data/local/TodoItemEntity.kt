package dev.maxsiomin.todoapp.feature.todolist.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Priority
import dev.maxsiomin.todoapp.feature.todolist.domain.model.Progress
import kotlinx.datetime.LocalDate

@Entity(tableName = "todoItems")
internal data class TodoItemEntity(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    @ColumnInfo("description")
    val description: String,

    @ColumnInfo("priority")
    val priority: Priority,

    @ColumnInfo("progress")
    val progress: Progress,

    @ColumnInfo("created")
    val created: LocalDate,

    @ColumnInfo("modified")
    val modified: LocalDate?,

    @ColumnInfo("deadline")
    val deadline: LocalDate?,
)