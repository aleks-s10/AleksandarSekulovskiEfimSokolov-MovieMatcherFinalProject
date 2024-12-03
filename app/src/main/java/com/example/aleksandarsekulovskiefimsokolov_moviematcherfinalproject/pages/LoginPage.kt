package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.fetchAndStoreMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var movies by remember { mutableStateOf<List<MovieDB>>(emptyList()) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        // Fetch and store movies from the API
        fetchAndStoreMovies(context, 1)

        // Retrieve movies from the database
        val db = DatabaseProvider.getDatabase(context)
        movies = withContext(Dispatchers.IO) {
            db.movieDao().getAllMovies()
        }

        // Handle authentication state
//        when (authState.value) {
//            is AuthState.Authenticated -> navController.navigate("library")
//            is AuthState.Error -> Toast.makeText(
//                context,
//                (authState.value as AuthState.Error).message,
//                Toast.LENGTH_SHORT
//            ).show()
//            else -> Unit
//        }
    }

    Column(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.secondaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome to MovieMatch!", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(text = "Email") },
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White)
        )
        Spacer(modifier = Modifier.height(0.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = { authViewModel.login(email, password) },
            enabled = authState.value != AuthState.Loading,
            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.navigate("signup") }) {
            Text(text = "Don't have an account? Sign up!")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Display all information about movies retrieved from the database
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movies) { movie ->
                Column(
                    modifier = Modifier.background(Color.White).padding(8.dp)
                ) {
                    Text(text = "Title: ${movie.title}", fontSize = 18.sp, color = Color.Black)
                    Text(text = "Description: ${movie.description}", fontSize = 14.sp, color = Color.DarkGray)
                    Text(text = "Genre: ${movie.genre}", fontSize = 14.sp, color = Color.Black)
                    Text(text = "Release Year: ${movie.release_year}", fontSize = 14.sp, color = Color.Black)
                    Text(text = "Rating: ${movie.rating}", fontSize = 14.sp, color = Color.Black)
                    Text(text = "Poster Path: ${movie.poster}", fontSize = 12.sp, color = Color.Gray)
                }
            }
        }
    }
}
