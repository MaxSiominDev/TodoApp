package dev.maxsiomin.todoapp.feature.todolist.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TodoDao {

    @Query(value = "SELECT * FROM todoitems")
    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    @Upsert
    suspend fun upsertTodoItem(item: TodoItemEntity)

}