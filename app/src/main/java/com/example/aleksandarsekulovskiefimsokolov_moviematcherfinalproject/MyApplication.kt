package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import android.app.Application
import androidx.work.Configuration
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.workers.NotificationWorkerFactory
import com.google.firebase.FirebaseApp

class MyApplication : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }

    // Lazily initialize your NotificationsViewModel here
    val notificationsViewModel: NotificationsViewModel by lazy {
        NotificationsViewModel()
    }

    // Lazily initialize your NotificationWorkerFactory here
    val notificationWorkerFactory: NotificationWorkerFactory by lazy {
        NotificationWorkerFactory()
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .setWorkerFactory(notificationWorkerFactory)
            .build()


}