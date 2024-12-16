package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.*
import com.example.aleksandarsekulov_moviematcherfinalproject.NotificationsViewModelFactory
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.MyApplication
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.NotificationsViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.workers.NotificationWorker

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit
import androidx.compose.foundation.Image

import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale

import androidx.compose.ui.res.painterResource

import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

import androidx.compose.material3.Card

import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.CardDefaults
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.GroupDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDao
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun GroupNotificationCard(
    groupName: String,
    memberProfilePictures: List<Int>,
    onStartSessionClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = groupName,
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.weight(1f)
                )
                Button(
                    onClick = onStartSessionClick,
                    modifier = Modifier.wrapContentWidth()
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Start Session",
                        modifier = Modifier.size(35.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Session")
                }
            }
            Spacer(Modifier.height(5.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                memberProfilePictures.forEach { profilePicRes ->
                    Image(
                        painter = painterResource(id = profilePicRes),
                        contentDescription = "Group member profile picture",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

@Composable
fun GroupNotificationsList(
    groups: List<GroupDB>,
    onStartSessionClick: (GroupDB) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(groups) { group ->
            GroupNotificationCard(
                groupName = group.sessionName,
                memberProfilePictures = listOf(R.drawable.joker),
                onStartSessionClick = { onStartSessionClick(group) }
            )
        }
    }
}

suspend fun acceptGroupRequest(
    group: GroupDB,
    authViewModel: AuthViewModel,
    notificationsViewModel: NotificationsViewModel,
    localDb: MovieDao
) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = authViewModel.currentAppUser

    db.runBatch { batch ->
        val currentUserRef = db.collection("users").document(currentUser)

        batch.update(currentUserRef, "Sessions", FieldValue.arrayUnion(group.groupID))
        val requestRef = db.collection("sessionsRequests").document(currentUser)
        batch.update(requestRef, "Requests", FieldValue.arrayRemove(group.groupID))
        notificationsViewModel.deleteGroupRequest(group.groupID)
    }.addOnSuccessListener {
        CoroutineScope(Dispatchers.IO).launch {
            localDb.insertGroupNotification(group.copy(pending = false))
        }
        Log.d("Firestore", "Session added")

    }.addOnFailureListener { e ->
        Log.e("Firestore", "Error adding session", e)
    }
}



@Composable
fun FriendRequestCard(
    user: UserDB,
    onAddFriendClick: (UserDB) -> Unit,
    modifier: Modifier = Modifier,
    showIcon: Boolean
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.elevatedCardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.joker),
                contentDescription = "${user.firstName} ${user.lastName} Profile Picture",
                modifier = Modifier
                    .size(55.dp)
                    .clip(RoundedCornerShape(50)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${user.firstName} ${user.lastName}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "@${user.userName}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Favorite genre: ${user.favoriteGenre}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            if (showIcon) {
                IconButton(
                    onClick = { onAddFriendClick(user) }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add ${user.userName} as a friend",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}



suspend fun acceptFriendRequest(
    user: UserDB,
    authViewModel: AuthViewModel,
    notificationsViewModel: NotificationsViewModel,
    localDb: MovieDao
) {
    val db = FirebaseFirestore.getInstance()
    val currentUser = authViewModel.currentAppUser

    db.runBatch { batch ->
        val userRef = db.collection("users").document(user.userID)
        val currentUserRef = db.collection("users").document(currentUser)

        batch.update(userRef, "Friends", FieldValue.arrayUnion(currentUser))
        batch.update(currentUserRef, "Friends", FieldValue.arrayUnion(user.userID))

        val requestRef = db.collection("friendRequests").document(currentUser)
        batch.update(requestRef, "Requesters", FieldValue.arrayRemove(user.userID))

        notificationsViewModel.deleteFriendRequest(user.userID)
    }.addOnSuccessListener {
        Log.d("Firestore", "Both users added as friends successfully")
        CoroutineScope(Dispatchers.IO).launch {
            localDb.insertUser(user.copy(pending = false, self = 0))
        }

    }.addOnFailureListener { e ->
        Log.e("Firestore", "Error adding friends", e)
    }
}

@Composable
fun NotificationsPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    notificationsViewModel: NotificationsViewModel = viewModel(
        factory = NotificationsViewModelFactory(
            (LocalContext.current.applicationContext as MyApplication).notificationsViewModel
        )
    )
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val friendNotifications by notificationsViewModel.friendNotifications.collectAsState(initial = emptyList())
    val groupNotifications by notificationsViewModel.groupNotifications.collectAsState(initial = emptyList())
    val localDb = DatabaseProvider.getDatabase(context).movieDao()

    // WorkManager setup
    LaunchedEffect(key1 = lifecycleOwner) {
        lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                val constraints = Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()

                // Proceed with enqueuing the work request
                val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
                    20, TimeUnit.SECONDS // Consider increasing this interval
                )
                    .setConstraints(constraints)
                    .build()

                WorkManager.getInstance(context.applicationContext)
                    .enqueueUniquePeriodicWork(
                        "NotificationWork",
                        ExistingPeriodicWorkPolicy.KEEP,
                        notificationWorkRequest
                    )
            }
        })
    }

    // Display Notifications
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = modifier.padding(16.dp)) {
            Text("Friend Requests")
            LazyColumn {
                items(friendNotifications) { user ->
                    FriendRequestCard(
                        user = user,
                        onAddFriendClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                acceptFriendRequest(user, authViewModel, notificationsViewModel, localDb)
                            }
                                           },
                        showIcon = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Session Requests")
            GroupNotificationsList(
                groups = groupNotifications,
                onStartSessionClick = {
                    CoroutineScope(Dispatchers.IO).launch {
                        acceptGroupRequest(it, authViewModel, notificationsViewModel, localDb)
                    }}
            )
        }

        IconButton(
            onClick = { navController.navigate("profile") },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 20.dp, end = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
    }
}