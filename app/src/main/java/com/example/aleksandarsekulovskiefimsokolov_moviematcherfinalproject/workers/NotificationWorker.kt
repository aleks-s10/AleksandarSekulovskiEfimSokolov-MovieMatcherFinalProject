package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.workers

import android.content.Context
import android.util.Log
import androidx.room.PrimaryKey
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.GroupDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDatabase
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import kotlin.String

class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    CoroutineWorker(appContext, workerParams) {

    private suspend fun putFriendsNotificationsInDB(friendRequests: List<UserDB>, localDB: MovieDatabase) {
        for (user in friendRequests) {
            localDB.movieDao().insertUser(user)
        }
    }

    private suspend fun putGroupNotificationsInDB(groups: List<GroupDB>, localDB: MovieDatabase) {
        for (group in groups) {
            localDB.movieDao().insertGroupNotification(group)
        }
    }

    override suspend fun doWork(): Result = coroutineScope {
        val localDB = DatabaseProvider.getDatabase(applicationContext)
        try {
            // Fetch data from Firebase in a background thread
            val friendRequests = fetchFriendNotifications()
            val groups = fetchGroupNotifications()
            putFriendsNotificationsInDB(friendRequests, localDB)
            putGroupNotificationsInDB(groups, localDB)
            Log.d("NotificationWorker", "Notifications fetched successfully")
            Result.success()
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error fetching notifications", e)
            Result.retry() // Or Result.failure() if you don't want retries
        }
    }

    private suspend fun fetchFriendNotifications(): List<UserDB> = coroutineScope {
        val db = FirebaseFirestore.getInstance()
        val userId = Firebase.auth.currentUser ?: ""
        val friendRequests = mutableListOf<UserDB>()

        try {
            val document = db.collection("friendRequests").document(userId.toString()).get().await()
            if (document.exists()) {
                val requesters = document.get("Requesters") as? List<String>

                val deferredUserFetches = requesters?.map { senderId ->
                    async { // Use async for each user fetch
                        try {
                            val userDocument = db.collection("users").document(senderId.toString()).get().await()

                            // change userdb to firebaseusersdb

                            UserDB(
                                userID = userDocument.id,
                                userName = userDocument.getString("Username") ?: "Unknown",
                                profilePicture = userDocument.getLong("ProfilePicture")?.toInt() ?: 0,
                                movies = userDocument.get("Movies") as? List<String> ?: listOf(),
                                sessions = userDocument.get("Sessions") as? List<String> ?: listOf(),
                                email = userDocument.getString("Email") ?: "",
                                firstName = userDocument.getString("FirstName") ?: "",
                                lastName = userDocument.getString("LastName") ?: "",
                                favoriteGenre = userDocument.getString("FavoriteGenre") ?: "Unknown",
                                pending = true,
                                self = 0
                            )
                        } catch (e: Exception) {
                            Log.e("NotificationWorker", "Error getting user document: $senderId", e)
                            null // Handle individual user fetch failure
                        }
                    }
                }

                // Wait for all user fetches to complete and filter out nulls (failed fetches)
                friendRequests.addAll((deferredUserFetches?.awaitAll() ) as Collection<UserDB>)
            }
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error getting documents: ", e)
            throw e // Re-throw to allow WorkManager to handle retry
        }

        return@coroutineScope friendRequests
    }
    private suspend fun fetchGroupNotifications(): List<GroupDB> = coroutineScope {
        val db = FirebaseFirestore.getInstance()
        val userId = Firebase.auth.currentUser?: ""
        val groups = mutableListOf<GroupDB>()

        try {
            val document = db.collection("sessionsRequests").document(userId.toString()).get().await()
            if (document.exists()) {
                val requesters = document.get("Requests") as? List<String>

                val deferredGroupFetches = requesters?.map { sessionId ->
                    async { // Use async for each fetch
                        try {
                            val session = db.collection("sessions").document(sessionId.toString()).get().await()


                            GroupDB(
                                groupID = sessionId,
                                sessionName = session.get("sessionName").toString(),
                                users = (session.get("Users") as? Map<String, List<String>> ?: listOf("")) as Map<String, List<String>>,
                                pending = true,
                                movies = session.get("movies") as Map<String, List<Int>>,
                                numUsers = session.get("numUsers") as Int,
                                finalMovie = "",
                            )
                        } catch (e: Exception) {
                            Log.e("NotificationWorker", "Error getting user document: $sessionId", e)
                            null // Handle individual user fetch failure
                        }
                    }
                }

                // Wait for all user fetches to complete and filter out nulls (failed fetches)
                groups.addAll((deferredGroupFetches?.awaitAll() ) as Collection<GroupDB>)
            }
        } catch (e: Exception) {
            Log.e("NotificationWorker", "Error getting documents: ", e)
            throw e // Re-throw to allow WorkManager to handle retry
        }

        return@coroutineScope groups
    }
}