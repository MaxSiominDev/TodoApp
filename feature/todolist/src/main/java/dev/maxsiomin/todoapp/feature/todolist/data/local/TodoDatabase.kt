package dev.maxsiomin.todoapp.feature.todolist.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.maxsiomin.todoapp.feature.todolist.data.local.converters.LocalDateConverters
import dev.maxsiomin.todoapp.feature.todolist.data.local.converters.PriorityConverters
import dev.maxsiomin.todoapp.feature.todolist.data.local.migrations.MIGRATION_1_2

@Database(entities = [TodoItemEntity::class, DeletedItem::class], version = 4)
@TypeConverters(value = [PriorityConverters::class, LocalDateConverters::class])
internal abstract class TodoDatabase : RoomDatabase() {

    abstract val todoDao: TodoDao

    companion object {
        const val DATABASE_NAME = "todo_db"

        val migrations = arrayOf(MIGRATION_1_2)
    }
}
