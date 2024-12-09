package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthState
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel

@Composable
fun Homepage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    val authState = authViewModel.authState.observeAsState()

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Unauthenticated -> navController.navigate("login")
            else -> Unit
        }
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign out", fontSize = 32.sp)

        TextButton(onClick = {
            authViewModel.signout(navController)
        }) {
            Text(text = "Sign out")
        }
    }
}