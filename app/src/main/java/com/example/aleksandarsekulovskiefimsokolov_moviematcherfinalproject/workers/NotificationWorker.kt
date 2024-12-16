package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.MyApplication
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.GroupDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    private fun enqueueNextWork() {
        val nextRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(20, TimeUnit.SECONDS) // 20 seconds delay
            .build()

        WorkManager.getInstance(applicationContext).enqueue(nextRequest)
        Log.d("NotificationWorker", "Next worker scheduled in 20 seconds")
    }

    override suspend fun doWork(): Result = coroutineScope {
        val application = applicationContext as MyApplication
        val notificationsViewModel = application.notificationsViewModel
        Log.d("NotificationWorker", "Worker started")
        try {
            Log.d("NotificationWorker", "Made it to the first try")
            val friendRequests = fetchFriendNotifications()
            val groups = fetchGroupNotifications()

            Log.d("GROUPS" , groups.toString())
            Log.d("FRIENDS", friendRequests.toString())

            Log.d("NotificationWorker", "Notifications fetched successfully")
            // You can trigger a notification here or update the local database.

            enqueueNextWork()
            notificationsViewModel.updateNotifications(friendRequests, groups)

            Result.success()
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error fetching notifications", e)
            Result.retry()
        }
    }

    private suspend fun fetchFriendNotifications(): List<UserDB> = coroutineScope {
        val db = FirebaseFirestore.getInstance()
        val userId = Firebase.auth.currentUser?.email ?: return@coroutineScope emptyList()
        val friendRequests = mutableListOf<UserDB>()

        try {
            val document = db.collection("friendRequests").document(userId).get().await()
            val requesters = document.get("Requesters") as? List<String> ?: emptyList()

            val deferredFetches = requesters.map { senderId ->
                async {
                    try {
                        val userDoc = db.collection("users").document(senderId).get().await()
                        UserDB(
                            userID = userDoc.id,
                            userName = userDoc.getString("Username") ?: "Unknown",
                            profilePicture = userDoc.getLong("Profile_Picture")?.toInt() ?: 0,
                            movies = userDoc.get("Movies") as? List<String> ?: emptyList(),
                            sessions = userDoc.get("Sessions") as? List<String> ?: emptyList(),
                            email = userDoc.getString("email") ?: "",
                            firstName = userDoc.getString("FirstName") ?: "",
                            lastName = userDoc.getString("LastName") ?: "",
                            favoriteGenre = userDoc.getString("favGenre") ?: "Unknown",
                            pending = true,
                            self = 0
                        )
                    } catch (e: Exception) {
                        Log.e("NotificationWorker", "Error fetching user: $senderId", e)
                        null
                    }
                }
            }
            friendRequests.addAll(deferredFetches.awaitAll().filterNotNull())
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error fetching friend requests", e)
        }
        friendRequests
    }

    private suspend fun fetchGroupNotifications(): List<GroupDB> = coroutineScope {
        val db = FirebaseFirestore.getInstance()
        val userId = Firebase.auth.currentUser?.email ?: return@coroutineScope emptyList()
        val groups = mutableListOf<GroupDB>()

        try {
            val document = db.collection("sessionsRequests").document(userId).get().await()
            val requesters = document.get("Requests") as? List<String> ?: emptyList()

            val deferredFetches = requesters.map { sessionId ->
                async {
                    try {
                        val sessionDoc = db.collection("sessions").document(sessionId).get().await()
                        GroupDB(
                            groupID = sessionId,
                            users = sessionDoc.get("Users") as? Map<String, List<String>> ?: emptyMap(),
                            movies = sessionDoc.get("movies") as? Map<String, Int> ?: emptyMap(),
                            pending = true,
                            numUsers = sessionDoc.getLong("numUsers")?.toInt() ?: 0,
                            finalMovie = sessionDoc.getString("finalMovie") ?: "",
                            sessionName = sessionDoc.getString("sessionName") ?: ""
                        )
                    } catch (e: Exception) {
                        Log.e("NotificationWorker", "Error fetching session: $sessionId", e)
                        null
                    }
                }
            }
            groups.addAll(deferredFetches.awaitAll().filterNotNull())
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error fetching groups", e)
        }
        groups
    }
}
