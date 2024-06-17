package dev.maxsiomin.todoapp

import android.app.Application
import dev.maxsiomin.todoapp.di.DaggerAppComponent

class App : Application() {

    val appComponent by lazy {
        DaggerAppComponent.builder()
            .application(this)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        appComponent.inject(this)
    }
}