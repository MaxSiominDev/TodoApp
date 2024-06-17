package dev.maxsiomin.todoapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.maxsiomin.todoapp.data.local.converters.LocalDateConverters
import dev.maxsiomin.todoapp.data.local.converters.PriorityConverters
import dev.maxsiomin.todoapp.data.local.converters.ProgressConverters

@Database(entities = [TodoItemEntity::class], version = 1)
@TypeConverters(value = [ProgressConverters::class, PriorityConverters::class, LocalDateConverters::class])
abstract class TodoDatabase : RoomDatabase() {

    abstract val dao: TodoDao

    companion object {
        const val DATABASE_NAME = "todo_db"
    }
}
