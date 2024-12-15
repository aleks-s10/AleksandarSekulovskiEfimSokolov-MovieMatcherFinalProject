package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.runtime.mutableIntStateOf
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
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R

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

    var profilePicture by remember {
        mutableStateOf(0)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    var page by remember { mutableIntStateOf(1) }
    val pageUp = {if (page <= 3) page += 1}
    val pageDown = {page -= 1}

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = DatabaseProvider.getDatabase(context)

    val addSelfToDB: (UserDB) -> Unit = {
        coroutineScope.launch {
            db.movieDao().insertUser(it)
        }
    }

    val images = listOf(R.drawable.joker, R.drawable.joker, R.drawable.joker, R.drawable.joker, R.drawable.joker)

    var selectIndex: (Int) -> Unit = {
        profilePicture = it
    }

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.Authenticated -> navController.navigate("Trending")
            is AuthState.Error -> Toast.makeText(context,
                (authState.value as AuthState.Error).message,Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.secondaryContainer),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Signup", fontSize = 32.sp)

        Spacer(modifier = Modifier.height(16.dp))

        when (page){
            1 -> {
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
                Spacer(modifier = Modifier.height(3.dp))


                OutlinedTextField(
                    value = username,
                    onValueChange = {
                        username = it
                    },
                    label = {
                        Text(text = "Username")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
                )

                Spacer(modifier = Modifier.height(3.dp))

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
            }
            2 -> {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = {
                        firstName = it
                    },
                    label = {
                        Text(text = "First Name")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
                )

                Spacer(modifier = Modifier.height(3.dp))

                OutlinedTextField(
                    value = lastName,
                    onValueChange = {
                        lastName = it
                    },
                    label = {
                        Text(text = "Last Name")
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
                )

                Spacer(modifier = Modifier.height(3.dp))

                OutlinedTextField(
                    value = favoriteGenre,
                    onValueChange = { favoriteGenre = it },
                    label = { Text("Favorite Genre") },
                    modifier = Modifier
                        .clickable { expanded = true }, // Open dropdown when clicked
                    readOnly = true,
                    leadingIcon = {
                        Icon(
                            imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                            contentDescription = null,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = Color.White),
                    singleLine = true
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    genres.forEach { genre ->
                        DropdownMenuItem(
                            text = { Text(genre) },
                            onClick = {
                                favoriteGenre = genre
                                expanded = false
                            }
                        )
                    }
                }
            }
            3 -> {
                Column (horizontalAlignment = Alignment.CenterHorizontally){
                    ProfilePicturePicker(images, profilePicture, selectIndex)
                    OutlinedButton(
                        onClick = {
                            addSelfToDB(
                                UserDB(
                                    userID = email,
                                    userName = username,
                                    email = email,
                                    profilePicture = profilePicture,
                                    movies = listOf(),
                                    sessions = listOf(),
                                    firstName = firstName,
                                    lastName = lastName,
                                    favoriteGenre = favoriteGenre,
                                    pending = false,
                                    self = 1
                                )
                            )
                            authViewModel.signup(
                                email,
                                password,
                                username,
                                profilePicture,
                                firstName,
                                lastName,
                                favoriteGenre
                            )
                            email = ""
                            password = ""
                            username = ""
                        },
                        enabled = authState.value != AuthState.Loading,
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White)
                    ) {
                        Text(text = "Sign Up")
                    }

                }
                }
        }

        if (page == 1){
            IconButton(
                onClick = pageUp,
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Next page",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
        else if (page == 3){
            IconButton(onClick = pageDown) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Next Page",
                    modifier = Modifier.size(35.dp)
                )
            }
        }
        else{
            Row{
                IconButton(onClick = pageDown) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Next Page",
                        modifier = Modifier.size(35.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(onClick = pageUp) {
                    Icon(
                        imageVector = Icons.Filled.ArrowForward,
                        contentDescription = "Previous page",
                        modifier = Modifier.size(35.dp)
                    )
                }
            }
        }


        TextButton(onClick = {
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ProfilePicturePicker(
    images: List<Int>,
    selectedIndex: Int,
    selectIndex: (Int) -> Unit
) {
    val imageSize = 80
    FlowRow(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        images.forEachIndexed { index, image ->
            val isSelected = index == selectedIndex
            val elevation = if (isSelected) 8.dp else 0.dp

            Card(
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { selectIndex(index) }
                    .border(if (isSelected) 6.dp else 0.dp, Color.White, CircleShape),
                elevation = CardDefaults.elevatedCardElevation(elevation),
                colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = "Profile Picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(imageSize.dp)
                        .clip(CircleShape)
                        .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                )
            }
        }
    }
}
