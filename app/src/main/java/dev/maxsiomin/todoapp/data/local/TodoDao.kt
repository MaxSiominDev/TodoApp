package dev.maxsiomin.todoapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import dev.maxsiomin.todoapp.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query(value = "SELECT * FROM todoitems")
    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    @Upsert
    suspend fun upsertTodoItem(item: TodoItemEntity)

}