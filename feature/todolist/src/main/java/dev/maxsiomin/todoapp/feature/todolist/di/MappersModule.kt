package dev.maxsiomin.todoapp.feature.todolist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.maxsiomin.common.data.mappers.BidirectionalMapper
import dev.maxsiomin.todoapp.feature.todolist.data.local.TodoItemEntity
import dev.maxsiomin.todoapp.feature.todolist.data.mappers.TodoItemMapper
import dev.maxsiomin.todoapp.feature.todolist.domain.model.TodoItem

@InstallIn(ViewModelComponent::class)
@Module
internal abstract class MappersModule {

    @Binds
    @ViewModelScoped
    abstract fun bindTodoItemMapper(impl: TodoItemMapper): BidirectionalMapper<TodoItemEntity, TodoItem>

}