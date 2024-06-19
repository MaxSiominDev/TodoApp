package dev.maxsiomin.todoapp.feature.todolist.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dev.maxsiomin.todoapp.feature.todolist.data.JvmUuidGenerator
import dev.maxsiomin.todoapp.feature.todolist.domain.UuidGenerator

@InstallIn(ViewModelComponent::class)
@Module
abstract class DomainModule {

    @ViewModelScoped
    @Binds
    abstract fun bindUuidGenerator(impl: JvmUuidGenerator): UuidGenerator

}