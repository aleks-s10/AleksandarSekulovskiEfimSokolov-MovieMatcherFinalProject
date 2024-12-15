package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel

@Composable
fun NotificationsPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    @Composable
    fun NotificationsPage(
        modifier: Modifier = Modifier,
        navController: NavController,
        authViewModel: AuthViewModel,
        //notificationsViewModel: NotificationsViewModel = viewModel()
    ) {
//        val notifications by notificationsViewModel.notifications.collectAsState()
//        val lifecycleOwner = LocalLifecycleOwner.current
//
//        // WorkManager setup
//        LaunchedEffect(key1 = lifecycleOwner) {
//            lifecycleOwner.lifecycle.addObserver(LifecycleEventObserver { _, event ->
//                if (event == Lifecycle.Event.ON_START) {
//                    val constraints = Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
//                        .build()
//
//                    val notificationWorkRequest = PeriodicWorkRequestBuilder<NotificationWorker>(
//                        20, TimeUnit.SECONDS
//                    )
//                        .setConstraints(constraints)
//                        .build()
//
//                    WorkManager.getInstance(applicationContext)
//                        .enqueue(notificationWorkRequest)
//                }
//            })
//        }

        // ... rest of your composable ...
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = {
                    navController.navigate("profile")
                },
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
}