package dev.maxsiomin.todoapp.feature.todolist.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deletedItems")
data class DeletedItem(
    @PrimaryKey(autoGenerate = false)
    val id: String,
)
