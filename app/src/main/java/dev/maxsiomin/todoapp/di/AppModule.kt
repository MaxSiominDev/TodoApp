package dev.maxsiomin.todoapp.di

import android.content.Context
import androidx.room.Dao
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dev.maxsiomin.common.data.mappers.BidirectionalMapper
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDao
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.feature.todolist.data.mappers.TodoItemMapper
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Singleton
    @Provides
    fun provideContext(): Context = context

    @Singleton
    @Provides
    fun provideTodoDatabase(context: Context): dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase {
        return Room.databaseBuilder(
            context,
            klass = dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase::class.java,
            name = dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase.DATABASE_NAME,
        ).build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(database: dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase): dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDao {
        return database.dao
    }

    @Provides
    fun provideTodoItemMapper(): dev.maxsiomin.common.data.mappers.BidirectionalMapper<dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity, dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem> {
        return dev.maxsiomin.todoapp.feature.todolist.data.mappers.TodoItemMapper()
    }

}