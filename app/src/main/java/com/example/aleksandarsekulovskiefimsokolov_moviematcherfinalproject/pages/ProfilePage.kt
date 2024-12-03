package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
// Import statements (ensure you include all necessary imports)
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import coil.compose.SubcomposeAsyncImage
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
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


// Sample data
val sampleMovies = listOf(
    MovieDB(
        description = "Eddie and Venom are on the run.",
        genre = "Adventure",
        poster = "/aosm8NMQ3UyoBVpSxyimorCQykC.jpg",
        rating = 8.5,
        release_year = "2024",
        title = "Venom: The Last Dance",
        page = 1,
        id = "123"
    ),
    MovieDB(
        description = "A thrilling tale of mystery and suspense.",
        genre = "Thriller",
        poster = "/cNtAslrDhk1i3IOZ16vF7df6lMy.jpg",
        rating = 7.8,
        release_year = "2020",
        title = "Absolution",
        page = 2,
        id = "1234"
    ),
)

val sampleUser = User(
    firstName = "John",
    lastName = "Doe",
    username = "johndoe123",
    favoriteGenre = "Science Fiction",
    profileImageResId = R.drawable.moviepug, // Placeholder image resource
    recentMovies = sampleMovies
)

@Composable
fun ProfileScreen(user: User, navController: NavController) {
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
                        text = "${user.firstName} ${user.lastName}",
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
                            text = "@${user.username}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
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
                text = user.favoriteGenre,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = 32.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Recently Watched Movies",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(user.recentMovies) { movie ->
                    MovieItem(movie = movie)
                }
            }
        }
        FooterNavigation(navController = navController, modifier = Modifier.align(Alignment.BottomCenter))
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
