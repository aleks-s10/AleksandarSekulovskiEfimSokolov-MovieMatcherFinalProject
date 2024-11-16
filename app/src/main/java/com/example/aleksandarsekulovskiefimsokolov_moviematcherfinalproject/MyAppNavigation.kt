package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages.Homepage
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages.LoginPage
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages.SignupPage
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages.SwipingScreen

@Composable
fun MyAppNavigation(modifier : Modifier = Modifier, authViewModel: AuthViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login", builder = {
        composable("login") {
            LoginPage(modifier, navController, authViewModel)
        }
        composable("signup") {
            SignupPage(modifier, navController, authViewModel)
        }
        composable("home") {
            Homepage(modifier, navController, authViewModel)
        }
        composable("swiping") {
            SwipingScreen(modifier, navController, authViewModel)
        }
    })

}