package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel

@Composable
fun NotificationsPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
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