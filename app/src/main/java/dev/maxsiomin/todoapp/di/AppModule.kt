package dev.maxsiomin.todoapp.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.maxsiomin.todoapp.common.data.mappers.BidirectionalMapper
import dev.maxsiomin.todoapp.data.local.TodoDao
import dev.maxsiomin.todoapp.data.local.TodoDatabase
import dev.maxsiomin.todoapp.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.data.mappers.TodoItemMapper
import dev.maxsiomin.todoapp.domain.model.TodoItem
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideTodoDatabase(context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            klass = TodoDatabase::class.java,
            name = TodoDatabase.DATABASE_NAME,
        ).build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(database: TodoDatabase): TodoDao {
        return database.dao
    }

    @Provides
    fun provideTodoItemMapper(): BidirectionalMapper<TodoItemEntity, TodoItem> {
        return TodoItemMapper()
    }

}