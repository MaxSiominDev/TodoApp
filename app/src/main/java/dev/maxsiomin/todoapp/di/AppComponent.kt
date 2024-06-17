package dev.maxsiomin.todoapp.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dev.maxsiomin.todoapp.App

@Component
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(context: Context): Builder

        fun build(): AppComponent
    }

    fun inject(app: App)

}