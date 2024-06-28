package dev.maxsiomin.todoapp.feature.todolist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.maxsiomin.todoapp.feature.todolist.data.remote.TodoItemsApi
import dev.maxsiomin.todoapp.feature.todolist.data.remote.TodoItemsApiImpl

@Module
@InstallIn(ViewModelComponent::class)
abstract class NetworkModule {

    @ViewModelScoped
    @Binds
    abstract fun bindTodoItemsApi(impl: TodoItemsApiImpl): TodoItemsApi

}