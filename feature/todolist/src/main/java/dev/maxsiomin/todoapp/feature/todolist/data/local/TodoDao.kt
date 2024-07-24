package dev.maxsiomin.todoapp.feature.todolist.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {

    @Query(value = "SELECT * FROM todoitems")
    fun getAllTodoItems(): Flow<List<TodoItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<TodoItemEntity>)

    @Query(value = "DELETE FROM todoItems")
    suspend fun clear()

    @Upsert
    suspend fun upsertTodoItem(item: TodoItemEntity)

    @Query(value = "SELECT * FROM todoItems WHERE id = :id")
    suspend fun getTodoItemById(id: String): TodoItemEntity?

    @Delete
    suspend fun deleteTodoItem(item: TodoItemEntity)

    @Query(value = "SELECT * FROM deletedItems")
    suspend fun getDeletedItems(): List<DeletedItem>

    @Upsert
    suspend fun addDeletedItem(item: DeletedItem)

}