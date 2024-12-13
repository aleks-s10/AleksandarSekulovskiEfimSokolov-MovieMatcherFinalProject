package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.UserDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.fetchAndStoreMovies
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.Int

val base_url = "https://image.tmdb.org/t/p/w500"

@Composable
fun TrendingCard(movieTitle: String, modifier: Modifier = Modifier, fontSize: Int) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Gray.copy(alpha = 0.9f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = movieTitle,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}



@Composable
fun TrendingItem(movie: MovieDB, modifier: Modifier = Modifier, setCurrentMovie: (MovieDB) -> Unit,
                 favorites: Set<MovieDB>,
                 setFavorite: (MovieDB) -> Unit,
                 setUnFavorite: (MovieDB) -> Unit) {
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = modifier
            .padding(8.dp)
            .height(250.dp)
            .aspectRatio(0.75f)
            .clickable {
                setCurrentMovie(movie)
            }
    ) {
        SubcomposeAsyncImage(
            model = "${base_url}${movie.poster}",
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.aspectRatio(0.75f).height(250.dp).clip(RectangleShape),
            loading = { CircularProgressIndicator() },
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

        }, modifier = Modifier.align(Alignment.TopStart).padding(top = 2.dp, start = 5.dp)) {
            Icon(
                imageVector = if (favorites.contains(movie)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Add Movie",
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
        }
        Row(Modifier.align(Alignment.TopEnd).padding(top = 5.dp, end = 5.dp)) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = Color.White)
            Text(
                text = String.format("%.1f", movie.rating),
                modifier.padding(start = 10.dp),
                color = Color.White
            )
        }
        TrendingCard(
            movieTitle = movie.title,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.BottomCenter),
            fontSize = 15
        )
    }
}
@Composable
fun TrendingContent(movies: List<MovieDB>, setCurrentMovie: (MovieDB) -> Unit,
                    favorites: Set<MovieDB>,
                    setFavorite: (MovieDB) -> Unit,
                    setUnFavorite: (MovieDB) -> Unit) {
    LazyColumn(
        Modifier.padding(bottom = 50.dp)
    ) {
        items(movies.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                it.forEach { movieIndex ->
                    TrendingItem(movieIndex, setCurrentMovie = setCurrentMovie,
                        favorites = favorites,
                        setFavorite = setFavorite,
                        setUnFavorite = setUnFavorite)
                }
            }
        }
    }
}


@Composable
fun Trending(setCurrentMovie: (MovieDB) -> Unit, navController: NavController,
            pageUp: () -> Unit, pageDown: () -> Unit, movies: List<MovieDB>, page: Int,
             favorites: Set<MovieDB>,
             setFavorite: (MovieDB) -> Unit,
             setUnFavorite: (MovieDB) -> Unit){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            TrendingHeader(
                title = "Trending Now",
                firstPage = page == 1,
                leftButtonHandler = pageDown,
                rightButtonHandler = pageUp
            )
            TrendingContent(movies, setCurrentMovie = setCurrentMovie,  favorites = favorites,
            setFavorite = setFavorite,
            setUnFavorite = setUnFavorite)
        }
        FooterNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}

@Composable
fun TrendingPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var page by remember { mutableIntStateOf(1) }
    val pageUp = {page += 1}
    val pageDown = {page -= 1}
    val placeHolderMovie = MovieDB("", "", "", "", 1.2, "", "", 1, favorite = false)
    var currentMovie by remember { mutableStateOf<MovieDB>(placeHolderMovie) }
    var detailsView by remember { mutableStateOf(false) }
    val setCurrentMovie: (MovieDB) -> Unit = {
        currentMovie = it
        detailsView = true
    }
    val closeDetails = { detailsView = false }

    val context = LocalContext.current
    val localDB = DatabaseProvider.getDatabase(context)

    val db = FirebaseFirestore.getInstance()

    val currentUser = authViewModel.currentAppUser
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {

        coroutineScope.launch { // Use CoroutineScope for network calls

            val document = db.collection("users").document(currentUser).get().await()

            if (document.exists()) {

                val user = UserDB(

                    userID = document.getString("id") ?: "",

                    email = document.getString("email") ?: "",

                    userName = document.getString("Username") ?: "",

                    firstName = document.getString("FirstName") ?: "",

                    lastName = document.getString("LastName") ?: "",

                    profilePicture = document.getLong("Profile_Picture")?.toInt() ?: 0,

                    favoriteGenre = document.getString("favGenre") ?: "",

                    movies = document.get("Movies") as? List<String> ?: listOf(),

                    sessions = document.get("Sessions") as? List<String> ?: listOf(),

                    pending = false,

                    self = 1,

                    )

                val friends = document.get("Friends") as? List<String> ?: listOf()


                friends.forEach { friendId ->

                    coroutineScope.launch { // Nested launch for each friend

                        val friendDoc = db.collection("users").document(friendId).get().await()

                        println("FRIENDDOC" + friendDoc)
                        if (friendDoc.exists()) {

                            val friend = UserDB(

                                userID = friendDoc.getString("id") ?: "",

                                email = friendDoc.getString("email") ?: "",

                                userName = friendDoc.getString("Username") ?: "",

                                firstName = friendDoc.getString("FirstName") ?: "",

                                lastName = friendDoc.getString("LastName") ?: "",

                                profilePicture = friendDoc.getLong("Profile_Picture")?.toInt() ?: 0,

                                favoriteGenre = friendDoc.getString("favGenre") ?: "",

                                movies = friendDoc.get("Movies") as? List<String> ?: listOf(),

                                sessions = friendDoc.get("Sessions") as? List<String> ?: listOf(),

                                pending = false,

                                self = 0,

                                )
                            println("FRIEND" + friend)

                            localDB.movieDao().insertUser(friend)

                        }

                    }

                }


                localDB.movieDao().insertUser(user)

            }

        }
    }


    var favorites by remember { mutableStateOf<Set<MovieDB>>(sampleMovies.toSet()) }
    val setFavorite: (MovieDB) -> Unit = {favorites = favorites + it}
    val setUnFavorite: (MovieDB) -> Unit = { movie ->
        favorites = favorites.filter {
            it != movie
        }.toSet()
    }

    LaunchedEffect(Unit) {
        favorites = localDB.movieDao().getFavorites().toSet()
    }

    var movies by remember { mutableStateOf<List<MovieDB>>(emptyList()) }
    LaunchedEffect(page) {
        movies = withContext(Dispatchers.IO) {
            localDB.movieDao().getMoviesByPage(page)
        }
        if (movies.isEmpty()){
            fetchAndStoreMovies(context, page.toInt())
            movies = withContext(Dispatchers.IO) {
                localDB.movieDao().getMoviesByPage(page)
            }
        }
    }

    if (!detailsView){
        Trending(
            setCurrentMovie = setCurrentMovie,
            navController = navController,
            page = page,
            pageUp = pageUp,
            pageDown = pageDown,
            movies = movies,
            favorites = favorites,
            setFavorite = setFavorite,
            setUnFavorite = setUnFavorite
        )
    }
    else{
        MovieDetails(
            movie = currentMovie,
            closeDetails = closeDetails,
            favorites = favorites,
            setFavorite = setFavorite,
            setUnFavorite = setUnFavorite
        )
    }
}


//
//
//@Preview
//@Composable
//fun TrendingPagePreview(){
//    TrendingPage()
//}

@Preview
@Composable
fun ImagePreview(){
    SubcomposeAsyncImage(
        model = "$base_url/yh64qw9mgXBvlaWDi7Q9tpUBAvH.jpg",
        contentDescription = "Image",
        modifier = Modifier.fillMaxWidth(),
        loading = { CircularProgressIndicator() },
    )
}


@Composable
fun MovieDetails(movie: MovieDB, closeDetails: () -> Unit, favorites: Set<MovieDB>,
                 setFavorite: (MovieDB) -> Unit, setUnFavorite: (MovieDB) -> Unit ) {
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        BackGroundPoster(movie = movie)
        ForegroundPoster(movie = movie)
        IconButton(onClick = closeDetails, modifier = Modifier.align(Alignment.TopEnd).padding(top = 15.dp)) {
            Icon(
                imageVector = Icons.Filled.Close,
                contentDescription = "Add Movie",
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
        }
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

        }, modifier = Modifier.align(Alignment.TopStart).padding(top = 15.dp)) {
            Icon(
                imageVector = if (favorites.contains(movie)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = "Add Movie",
                modifier = Modifier.size(35.dp),
                tint = Color.White
            )
        }
        Column(
            Modifier
                .padding(start = 20.dp, end = 20.dp, bottom = 50.dp)
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = movie.title,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 38.sp,
                color = Color.White,
                lineHeight = 40.sp,
                textAlign = TextAlign.Center
            )
            Rating(movie = movie, modifier = Modifier)
            TextBuilder(icon = Icons.Filled.Info, title = "Description:", bodyText = movie.description)
            TextBuilder(icon = Icons.Filled.Create, title = "Genre:", bodyText = movie.genre)
        }
    }
}

@Composable
fun TextBuilder(icon: ImageVector, title: String, bodyText: String) {
    Row {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White
        )
        Text(
            text = title,
            Modifier.padding(start = 10.dp),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
    Text(text = bodyText, color = Color.White)
}

@Composable
fun Rating(movie: MovieDB, modifier: Modifier = Modifier) {
    Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        Icon(imageVector = Icons.Filled.Star, contentDescription = "", tint = Color.White)
        Text(
            text = movie.rating.toString(),
            modifier.padding(start = 6.dp),
            color = Color.White
        )
        Spacer(modifier = modifier.width(25.dp))
        Icon(imageVector = Icons.Filled.DateRange, contentDescription = "", tint = Color.White)
        Text(
            text = movie.release_year,
            modifier.padding(start = 6.dp),
            color = Color.White
        )
    }
}

@Composable
fun ForegroundPoster(movie: MovieDB) {
    Box(
        modifier = Modifier
            .wrapContentHeight()
            .width(250.dp)
            .padding(top = 80.dp)
            .clip(RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.TopCenter
    ) {
        SubcomposeAsyncImage(
            model = "${base_url}${movie.poster}",
            contentDescription = movie.title,
            modifier = Modifier
                .width(250.dp)
                .clip(RoundedCornerShape(16.dp)),
            loading = { CircularProgressIndicator() },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xB91A1B1B),
                        )
                    ), shape = RoundedCornerShape(16.dp)
                )
        )
    }
}

@Composable
fun BackGroundPoster(movie: MovieDB) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        SubcomposeAsyncImage(
            model = "${base_url}${movie.poster}",
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(0.6f),
            loading = { CircularProgressIndicator() },
        )
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.DarkGray
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        )
    }
}