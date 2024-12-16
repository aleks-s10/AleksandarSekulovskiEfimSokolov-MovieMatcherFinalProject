package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.NotificationsViewModel


class NotificationWorkerFactory : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            NotificationWorker::class.java.name -> NotificationWorker(appContext, workerParameters)
            else -> null
        }
    }
}

