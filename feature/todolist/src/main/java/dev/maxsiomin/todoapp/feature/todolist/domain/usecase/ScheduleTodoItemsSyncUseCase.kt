package dev.maxsiomin.todoapp.feature.todolist.domain.usecase

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.maxsiomin.todoapp.feature.todolist.data.SyncItemsWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class ScheduleTodoItemsSyncUseCase @Inject constructor(
    @ApplicationContext private val context: Context
) {

    operator fun invoke() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val fetchItemsRequest = PeriodicWorkRequestBuilder<SyncItemsWorker>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SyncItemsWorker.UNIQUE_ID,
            ExistingPeriodicWorkPolicy.KEEP,
            fetchItemsRequest,
        )
    }

}