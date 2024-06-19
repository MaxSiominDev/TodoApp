package dev.maxsiomin.todoapp.feature.todolist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
internal interface TodoDao {

    @Query(value = "SELECT * FROM todoitems")
    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    @Upsert
    suspend fun upsertTodoItem(item: TodoItemEntity)

    @Query(value = "SELECT * FROM todoItems WHERE id = :id")
    suspend fun getTodoItemById(id: String): TodoItemEntity?

    @Delete
    suspend fun deleteTodoItem(item: TodoItemEntity)

}