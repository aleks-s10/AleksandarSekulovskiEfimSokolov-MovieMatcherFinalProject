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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.DatabaseProvider
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.fetchAndStoreMovies
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
fun TrendingItem(movie: MovieDB, modifier: Modifier = Modifier, setCurrentMovie: (MovieDB) -> Unit) {
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
fun TrendingContent(movies: List<MovieDB>, setCurrentMovie: (MovieDB) -> Unit) {
    LazyColumn {
        items(movies.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                it.forEach { movieIndex ->
                    TrendingItem(movieIndex, setCurrentMovie = setCurrentMovie)
                }
            }
        }
    }
}


@Composable
fun Trending(setCurrentMovie: (MovieDB) -> Unit, navController: NavController,
            pageUp: () -> Unit, pageDown: () -> Unit, movies: List<MovieDB>, page: Int){
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            TrendingHeader(
                title = "Trending Now",
                firstPage = page == 1,
                leftButtonHandler = pageUp,
                rightButtonHandler = pageDown
            )
            TrendingContent(movies, setCurrentMovie = setCurrentMovie)
        }
        FooterNavigation(
            modifier = Modifier.align(Alignment.BottomCenter),
            navController = navController
        )
    }
}

@Composable
fun TrendingPage(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel){
    var page by remember { mutableStateOf(1) }
    val pageUp = {page = page + 1}
    val pageDown = {page = page - 1}
    val placeHolderMovie = MovieDB("", "", "", "", 1.2, "", "", 1)
    var currentMovie by remember { mutableStateOf<MovieDB>(placeHolderMovie) }
    var detailsView by remember { mutableStateOf(false) }
    val setCurrentMovie: (MovieDB) -> Unit = {
        currentMovie = it
        detailsView = true
    }
    val closeDetails = { detailsView = false }

    var movies by remember { mutableStateOf<List<MovieDB>>(emptyList()) }
    val context = LocalContext.current
    val db = DatabaseProvider.getDatabase(context)
    LaunchedEffect(page) {
        movies = withContext(Dispatchers.IO) {
            db.movieDao().getMoviesByPage(page)
        }
        if (movies.isEmpty()){
            fetchAndStoreMovies(context, page)
            movies = withContext(Dispatchers.IO) {
                db.movieDao().getMoviesByPage(page)
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
            movies = movies)
    }
    else{
        MovieDetails(movie = currentMovie, closeDetails = closeDetails)
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
fun MovieDetails(movie: MovieDB, closeDetails: () -> Unit) {

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