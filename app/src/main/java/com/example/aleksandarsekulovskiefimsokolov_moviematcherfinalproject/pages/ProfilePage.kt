package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages


import android.graphics.Movie
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
// Import statements (ensure you include all necessary imports)
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.fetchAndStoreMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openjdk.tools.javac.jvm.Profile

// Assuming you have a placeholder image resource named 'profile_placeholder' in your drawable folder
// R.drawable.profile_placeholder

// Data classes
data class User(
    val firstName: String,
    val lastName: String,
    val username: String,
    val favoriteGenre: String,
    val profileImageResId: Int,
    val recentMovies: List<MovieDB>
)

val sampleMovies: List<MovieDB> = listOf()

val sampleUser = User(
    firstName = "John",
    lastName = "Doe",
    username = "johndoe123",
    favoriteGenre = "Science Fiction",
    profileImageResId = R.drawable.moviepug, // Placeholder image resource
    recentMovies = sampleMovies
)

val genres = listOf(
    "Action",
    "Adventure",
    "Comedy",
    "Drama",
    "Horror",
    "Romance",
    "Science Fiction",
    "Thriller",
    "Fantasy",
    "Animation"
)

@Composable
fun ProfileScreen(user: User, navController: NavController) {
    var editInProgress by remember { mutableStateOf(false) }

    var firstName by rememberSaveable { mutableStateOf(user.firstName) }
    var lastName by rememberSaveable { mutableStateOf(user.lastName) }
    var username by rememberSaveable { mutableStateOf(user.username) }
    var favoriteGenre by rememberSaveable { mutableStateOf(user.favoriteGenre) }

    var movies by remember { mutableStateOf<List<MovieDB>>(emptyList()) }
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    LaunchedEffect(Unit) {
        movies = withContext(Dispatchers.IO) {
            db.movieDao().getFavorites()
        }
    }

    val onSubmit: (User) -> Unit = {
        firstName = it.firstName
        lastName = it.lastName
        username = it.username
        favoriteGenre = it.favoriteGenre
    }

    val flipEdit = {editInProgress = !editInProgress}
    if (!editInProgress){
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = user.profileImageResId),
                        contentDescription = "Profile Picture",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "$firstName $lastName",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Username",
                                tint = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "@$username",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(110.dp))
                    IconButton(
                        onClick = flipEdit,
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit",
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Favorite Genre",
                        tint = Color.DarkGray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Favorite Genre:",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = favoriteGenre,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(start = 32.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Favorite Movies",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(movies.chunked(3)) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            it.forEach { movieIndex ->
                                MovieItem(movieIndex)
                            }
                        }
                    }
                }
            }
            FooterNavigation(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
    else{
        ProfileEdit(
            flipEdit = flipEdit,
            user = sampleUser,
            onSubmit = onSubmit
        )
    }
}

@Composable
fun ProfileEdit(
    user: User,
    onSubmit: (User) -> Unit,
    flipEdit: () -> Unit
) {

    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var username by remember { mutableStateOf(user.username) }
    var favoriteGenre by remember { mutableStateOf(user.favoriteGenre) }

    var expanded by remember { mutableStateOf(false) }

    val genres = listOf(
        "Action", "Adventure", "Comedy", "Drama", "Fantasy",
        "Horror", "Romance", "Science Fiction", "Thriller", "Animation"
    )
    Box() {
        IconButton(
            onClick = {
                onSubmit(user)
                flipEdit()
            },
            modifier = Modifier.align(Alignment.TopEnd).padding(top = 20.dp, end = 5.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Spacer(Modifier.height(50.dp))
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("First Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Last Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Username") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Ascii,
                    imeAction = ImeAction.Next
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = favoriteGenre,
                onValueChange = { favoriteGenre = it },
                label = { Text("Favorite Genre") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = null
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = true }, // Open dropdown when clicked
                readOnly = true,
                trailingIcon = {
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = null,
                        modifier = Modifier.clickable { expanded = !expanded }
                    )
                },
                singleLine = true
            )

            // Dropdown Menu
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

            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    val updatedUser = user.copy(
                        firstName = firstName,
                        lastName = lastName,
                        username = username,
                        favoriteGenre = favoriteGenre
                    )
                    onSubmit(updatedUser)
                    flipEdit()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Submit")
            }
        }
    }
}


@Composable
fun MovieItem(movie: MovieDB) {
    Column(
        modifier = Modifier
            .width(120.dp)
    ) {
        SubcomposeAsyncImage(
            model = "${base_url}${movie.poster}",
            contentDescription = movie.title,
            modifier = Modifier
                .height(180.dp)
                .clip(RoundedCornerShape(8.dp)),
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun ProfilePage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    ProfileScreen(user = sampleUser, navController = navController)
}

//@Preview
//@Composable
//fun MainScreen() {
//    ProfileScreen(user = sampleUser)
//}
//
