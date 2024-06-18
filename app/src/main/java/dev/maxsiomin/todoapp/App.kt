package dev.maxsiomin.todoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import dev.maxsiomin.common.util.isRelease
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (isRelease()) {
            Timber.plant(Timber.DebugTree())
        }
    }
}