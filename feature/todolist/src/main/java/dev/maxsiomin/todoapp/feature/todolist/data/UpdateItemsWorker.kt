package dev.maxsiomin.todoapp.feature.todolist.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dev.maxsiomin.todoapp.feature.todolist.domain.repository.TodoItemsRepository
import javax.inject.Inject

internal class UpdateItemsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var repo: TodoItemsRepository

    override suspend fun doWork(): Result {
        TODO()
    }

}