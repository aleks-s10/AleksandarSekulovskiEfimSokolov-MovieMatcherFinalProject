package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages


import android.graphics.Movie
import android.util.Log
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
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.fetchAndStoreMovies
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.openjdk.tools.javac.jvm.Profile

// Assuming you have a placeholder image resource named 'profile_placeholder' in your drawable folder
// R.drawable.profile_placeholder


val sampleMovies: List<MovieDB> = listOf()

val profilePicturePlaceholder = R.drawable.moviepug

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

fun updateUserInfo(
    userId: String,
    Movies: List<String>? = null,
    Profile_Picture: String? = null,
    Sessions: List<String>? = null,
    email: String? = null,
    username: String? = null,
    firstName: String? = null,
    lastName: String? = null,
    favoriteGenre: String? = null
) {
    val db = FirebaseFirestore.getInstance()

    // Create a map of fields to update
    val updateData = mutableMapOf<String, Any>()

    // Add only non-null fields to the map
    Movies?.let { updateData["Movies"] = it }
    Profile_Picture?.let { updateData["Profile_Picture"] = it }
    Sessions?.let { updateData["Sessions"] = it }
    email?.let { updateData["email"] = it }
    username?.let { updateData["username"] = it }
    firstName?.let { updateData["firstName"] = it }
    lastName?.let { updateData["lastName"] = it }
    favoriteGenre?.let { updateData["favoriteGenre"] = it }

    // Perform Firestore update only if there is data to update
    if (updateData.isNotEmpty()) {
        db.collection("users").document(userId)
            .update(updateData)
            .addOnSuccessListener {
                Log.d("FIRESTORE", "User data updated successfully for ID: $userId")
            }
            .addOnFailureListener { e ->
                Log.e("FIRESTORE", "Error updating user data", e)
            }
    } else {
        Log.w("FIRESTORE", "No data provided for update")
    }
}

@Composable
fun ProfileScreen(navController: NavController) {
    var editInProgress by remember { mutableStateOf(false) }

    var firstName by remember { mutableStateOf<String>("") }
    var lastName by remember { mutableStateOf<String>("")}
    var username by remember { mutableStateOf<String>("")}
    var favoriteGenre by remember { mutableStateOf<String>("")}

    var favorites by remember { mutableStateOf<Set<MovieDB>>(sampleMovies.toSet()) }
    val setFavorite: (MovieDB) -> Unit = {favorites = favorites + it}
    val setUnFavorite: (MovieDB) -> Unit = { movie ->
        favorites = favorites.filter {
            it != movie
        }.toSet()
    }

    var detailsView by remember { mutableStateOf(false) }
    val placeHolderMovie = MovieDB("", "", "", "", 1.2, "", "", 1, favorite = false)
    var currentMovie by remember { mutableStateOf<MovieDB>(placeHolderMovie) }
    val setCurrentMovie: (MovieDB) -> Unit = {
        currentMovie = it
        detailsView = true
    }
    val closeDetails = { detailsView = false }
    val emptyUser = UserDB(userID = "", userName = "Username", profilePicture = R.drawable.joker, movies = listOf(), sessions = listOf(), email = "", firstName = "", lastName = "", favoriteGenre = "", pending = false, self = 1)

    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    var profile by remember {
        mutableStateOf<UserDB>(emptyUser)
        mutableStateOf<UserDB>(emptyUser)
    }

    LaunchedEffect(Unit) {
        favorites = db.movieDao().getFavorites().toSet()
        val profile_temp = db.movieDao().getSelf()
        println("PROFILE IS: $profile_temp")
        firstName = profile.firstName
        lastName = profile.lastName
        favoriteGenre = profile.favoriteGenre
        profile = db.movieDao().getSelf()
        firstName = profile.firstName
        lastName = profile.lastName
        favoriteGenre = profile.favoriteGenre
    }

    val onSubmit: (UserDB) -> Unit = {
        firstName = it.firstName
        lastName = it.lastName
        favoriteGenre = it.favoriteGenre
        updateUserInfo(
            userId = it.userName,
            firstName = it.firstName,
            lastName = it.lastName,
            favoriteGenre = it.favoriteGenre
        )
    }

    val flipEdit = {editInProgress = !editInProgress}
    if (!editInProgress && !detailsView){
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Spacer(Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = profilePicturePlaceholder),
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
                        Text(
                            text = username,
                            style = MaterialTheme.typography.titleLarge
                        )
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
                    items(favorites.chunked(3)) {
                        Row(modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            it.forEach { movieIndex ->
                                MovieItem(
                                    movieIndex,
                                    favorites = favorites,
                                    setFavorite = setFavorite,
                                    setUnFavorite = setUnFavorite,
                                    setCurrentMovie = setCurrentMovie)
                            }
                        }
                    }
                }
            }
            FooterNavigation(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
    else if (detailsView){
        MovieDetails(
            movie = currentMovie,
            closeDetails = closeDetails,
            favorites = favorites,
            setFavorite = setFavorite,
            setUnFavorite = setUnFavorite
        )
    }
    else{
        ProfileEdit(
            flipEdit = flipEdit,
            user = profile,
            onSubmit = onSubmit
        )
    }
}

@Composable
fun ProfileEdit(
    user: UserDB,
    onSubmit: (UserDB) -> Unit,
    flipEdit: () -> Unit
) {

    var firstName by remember { mutableStateOf(user.firstName) }
    var lastName by remember { mutableStateOf(user.lastName) }
    var username by remember { mutableStateOf(user.userName) }
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
                        userName = username,
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
fun MovieItem(movie: MovieDB,
              favorites: Set<MovieDB>,
              setFavorite: (MovieDB) -> Unit,
              setUnFavorite: (MovieDB) -> Unit,
              setCurrentMovie: (MovieDB) -> Unit) {

    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = Modifier
            .width(120.dp)
    ) {
        Box {
            SubcomposeAsyncImage(
                model = "${base_url}${movie.poster}",
                contentDescription = movie.title,
                modifier = Modifier
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .clickable {
                        setCurrentMovie(movie)
                    },
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
            IconButton(onClick = {
                if (favorites.contains(movie)){
                    coroutineScope.launch {
                        db.movieDao().setUnFavorite(movie.id)
                    }
                    setUnFavorite(movie)
                }
                else{
                    coroutineScope.launch {
                        db.movieDao().setFavorite(movie.id)
                    }
                    setFavorite(movie)
                }

            }, modifier = Modifier.align(Alignment.TopStart).padding(top = 2.dp, start = 2.dp)) {
                Icon(
                    imageVector = if (favorites.contains(movie)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = "Add Movie",
                    modifier = Modifier.size(35.dp),
                    tint = Color.White
                )
            }
        }
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
    ProfileScreen(navController = navController)
}

//@Preview
//@Composable
//fun MainScreen() {
//    ProfileScreen(user = sampleUser)
//}
//
