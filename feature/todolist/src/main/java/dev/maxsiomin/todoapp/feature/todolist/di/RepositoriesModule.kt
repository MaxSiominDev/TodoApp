package dev.maxsiomin.todoapp.feature.todolist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.maxsiomin.todoapp.feature.todolist.data.repository.TodoItemsRepositoryImpl
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository

@Module
@InstallIn(ViewModelComponent::class)
internal interface RepositoriesModule {

    @Binds
    @ViewModelScoped
    fun bindTodoItemsRepository(impl: TodoItemsRepositoryImpl): TodoItemsRepository

}