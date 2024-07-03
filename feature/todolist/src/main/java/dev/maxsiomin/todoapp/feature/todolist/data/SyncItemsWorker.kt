package dev.maxsiomin.todoapp.feature.todolist.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.maxsiomin.common.domain.resource.Resource
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import javax.inject.Inject

internal class SyncItemsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repo: TodoItemsRepository

    override suspend fun doWork(): Result {
        val response = repo.enqueueMergeWithApi()
        return when (response) {
            is Resource.Error -> Result.retry()
            is Resource.Success -> Result.success()
        }
    }

    companion object {
        const val UNIQUE_ID = "syncTodoItems"
    }

}