package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.pages

import androidx.activity.viewModels
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.roundToInt

data class FirestoreMovieDB(
    val id: Int = 0,
    val description: String = "",
    val genre: String = "",
    val poster: String = "",
    val rating: Double = 0.0,
    val release_year: String = "",
    val title: String = ""
)

// Content takes a movie and displays it
@Composable
fun Content(movie: FirestoreMovieDB){
    SubcomposeAsyncImage(
        model = "${base_url}${movie.poster}",
        contentDescription = "Current image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop,
        loading = { CircularProgressIndicator() },
    )
}

@Composable
fun MovieTitleCard(movieTitle: String, modifier: Modifier = Modifier, fontSize: Int) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = movieTitle,
                fontSize = fontSize.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MovieItemCard(movieTitle: String, modifier: Modifier = Modifier, fontSize: Int) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
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
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MovieItem(movie: FirestoreMovieDB, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .padding(8.dp)
            .height(250.dp)
            .aspectRatio(0.75f)
    ) {
        SubcomposeAsyncImage(
            model = "${base_url}${movie.poster}",
            contentDescription = movie.title,
            modifier = Modifier
                .height(250.dp)
                .aspectRatio(0.75f)
                .clip(RectangleShape),
            contentScale = ContentScale.Crop
        )
        MovieItemCard(
            movieTitle = movie.title,
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.TopCenter),
            fontSize = 15
        )
    }
}

@Composable
fun MovieList(movies: List<FirestoreMovieDB>) {
    LazyColumn {
        items(movies.chunked(2)) { rowItems ->
            Row(modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { movie ->
                    MovieItem(movie)
                }
            }
        }
    }
}

@Composable
fun SwipingScreen(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    val db = FirebaseFirestore.getInstance()
    val moviesState = remember { mutableStateOf<List<FirestoreMovieDB>>(emptyList()) }

    // FETCH 10 MOVIES FROM FIRESTORE
    LaunchedEffect(Unit) {
        db.collection("movies")
            .limit(10)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val loadedMovies = querySnapshot.documents.mapNotNull {
                    it.toObject(FirestoreMovieDB::class.java)
                }
                moviesState.value = loadedMovies
            }
    }

    var offset by remember { mutableStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    var i by remember { mutableStateOf(0) }
    var liked by remember { mutableStateOf(mutableSetOf<Int>()) }
    var preview by remember { mutableStateOf(false) }
    val authState = authViewModel.authState.observeAsState()

    val onSwipeLeft: () -> Unit = {
        i++
    }
    val onSwipeRight: () -> Unit = {
        liked = liked.toMutableSet().apply { add(i.mod(moviesState.value.size)) }
        i++
    }
    val swipeThreshold: Float = 400f
    val sensitivityFactor: Float = 3f

    LaunchedEffect(dismissRight) {
        if (dismissRight) {
            delay(300)
            onSwipeRight.invoke()
            dismissRight = false
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft) {
            delay(300)
            onSwipeLeft.invoke()
            dismissLeft = false
        }
    }

    LaunchedEffect(i) {
        if (moviesState.value.isNotEmpty() && i > 0 && i.mod(moviesState.value.size) == 0){
            preview = true
        }
    }

    if (moviesState.value.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
    }

    val currentMovie = moviesState.value[i.mod(moviesState.value.size)]

    if (preview){
        Column (modifier = Modifier.fillMaxSize()){
            TextButton(
                onClick = {
                    preview = false
                },
                modifier = Modifier.padding(top = 20.dp)
            ) { Text(
                "Back",
                fontSize = 20.sp
            ) }
            MovieList(liked.map { moviesState.value[it] })
        }
    }
    else {
        Box {
            val nextMovie = moviesState.value[(i+1).mod(moviesState.value.size)]
            SubcomposeAsyncImage(
                model = "${base_url}${nextMovie.poster}",
                contentDescription = "Next image",
                modifier = Modifier.fillMaxWidth(),
            )
            Box(modifier = Modifier
                .offset { IntOffset(offset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onDragEnd = {
                        offset = 0f
                    }) { change, dragAmount ->
                        offset += (dragAmount / density) * sensitivityFactor
                        when {
                            offset > swipeThreshold -> {
                                dismissRight = true
                            }
                            offset < -swipeThreshold -> {
                                dismissLeft = true
                            }
                        }
                        if (change.positionChange() != Offset.Zero) change.consume()
                    }
                }
                .graphicsLayer(
                    alpha = 10f - animateFloatAsState(if (dismissRight) 1f else 0f).value,
                    rotationZ = animateFloatAsState(offset / 50).value
                )) {
                Content(currentMovie)
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                TextButton(
                    onClick = {
                        preview = true
                    },
                    modifier = Modifier.align(Alignment.TopStart).padding(top = 15.dp)
                ) { Text(
                    "Preview",
                    fontSize = 20.sp,
                    color = Color.White
                ) }
                IconButton(
                    onClick = { navController.navigate("groups")},
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }

                Column {
                    MovieTitleCard(
                        modifier = Modifier.padding(top = 50.dp),
                        movieTitle = currentMovie.title,
                        fontSize = 24
                    )
                    MovieTitleCard(
                        movieTitle = currentMovie.description,
                        fontSize = 20
                    )
                }

                Row(
                    Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp)
                ) {
                    OutlinedButton(
                        shape = CircleShape,
                        modifier = Modifier.size(130.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        onClick = {
                            dismissRight = true
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(100.dp),
                            painter = painterResource(R.drawable.thumbsup),
                            contentDescription = "Like",
                        )
                    }
                    Spacer(Modifier.width(100.dp))
                    OutlinedButton(
                        shape = CircleShape,
                        modifier = Modifier.size(130.dp),
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        onClick = {
                            dismissLeft = true
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(100.dp),
                            painter = painterResource(R.drawable.thumbsdown),
                            contentDescription = "Dislike",
                        )
                    }
                }
            }
        }
    }
}
