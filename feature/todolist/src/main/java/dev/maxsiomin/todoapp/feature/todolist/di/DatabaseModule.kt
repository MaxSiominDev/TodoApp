package dev.maxsiomin.todoapp.feature.todolist.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDao
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoDatabase
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
internal object DatabaseModule {

    @Singleton
    @Provides
    fun provideTodoDatabase(@ApplicationContext context: Context): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            TodoDatabase.DATABASE_NAME,
        ).addMigrations(*TodoDatabase.migrations).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideTodoDao(db: TodoDatabase): TodoDao {
        return db.todoDao
    }

}