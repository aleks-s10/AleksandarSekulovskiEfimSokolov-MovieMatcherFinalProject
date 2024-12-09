package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthState
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.User
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {

    var email by remember {
        mutableStateOf("")
    }

    var username by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var firstName by remember {
        mutableStateOf("")
    }
    var lastName by remember {
        mutableStateOf("")
    }

    var favoriteGenre by remember {
        mutableStateOf("")
    }


    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = DatabaseProvider.getDatabase(context)

    val addSelfToDB: (User) -> Unit = {
        coroutineScope.launch {
            db.movieDao().insertUser(it)
        }
    }

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message,Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }


    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Signup Page", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "Email")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )
        Spacer(modifier = Modifier.height(0.dp))


        OutlinedTextField(
            value = username,
            onValueChange = {
                username  = it
            },
            label = {
                Text(text = "Username")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
        )

        Spacer(modifier = Modifier.height(0.dp))

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "Password")
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = {
            authViewModel.signup(email,password, username)
            email = ""
            password = ""
            username = ""
        },
            enabled = authState.value != AuthState.Loading,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Text(text = "Sign Up")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
//            addSelfToDB(
//                User(
//                    userID = email,
//                    userName = username,
//                    email = email,
//                    profilePicture = 0,
//                    movies =
//                )
//            )
            navController.navigate("login")
        }) {
            Text(text = "Already have an account? Login!")
        }

        TextButton(onClick = {
            throw RuntimeException("Test Crash")
        }) {
            Text(text = "Test Crash")
        }

    }
}