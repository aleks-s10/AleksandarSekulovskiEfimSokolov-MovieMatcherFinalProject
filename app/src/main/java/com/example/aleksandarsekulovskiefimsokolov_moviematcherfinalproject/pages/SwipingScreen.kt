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
import kotlin.math.roundToInt
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.FirestoreMovieDB

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
fun SwipingScreen(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel, sessionId : String) {
    val db = FirebaseFirestore.getInstance()
    val moviesState = remember { mutableStateOf<List<FirestoreMovieDB>>(emptyList()) }
    val seenMovies = remember { mutableStateOf<List<Int>>(emptyList()) }

    val userId = authViewModel.currentAppUser

    val finalMovieState = remember { mutableStateOf("") }
    val numUsersState = remember { mutableStateOf<Int?>(null) }
    val fetchingMoviesAllowed = remember { mutableStateOf(true) }

    // Function to get seen movies for current user in session
    fun updateSeenMoviesList() {
        db.collection("sessions")
            .document(sessionId)
            .get()
            .addOnSuccessListener { document ->
                val usersMap = document.get("users") as? Map<String, List<String>>
                seenMovies.value = usersMap?.get(userId)?.map { it.toInt() } ?: emptyList()
            }
    }

    fun fetchRandomMovies() {
        if (!fetchingMoviesAllowed.value) return
        updateSeenMoviesList()

        val availableMovieIds = (1..100).filter { it !in seenMovies.value }
        if (availableMovieIds.isEmpty()) {
            return
        }

        val randomIds = availableMovieIds.shuffled().take(10)

        db.collection("movies")
            .whereIn("id", randomIds)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val loadedMovies = querySnapshot.documents.mapNotNull {
                    it.toObject(FirestoreMovieDB::class.java)
                }
                moviesState.value = moviesState.value + loadedMovies
            }
    }

    val triggerFetchMovies = remember { mutableStateOf(false) }

    // get finalMovie and numUsers from db
    LaunchedEffect(Unit) {
        db.collection("sessions")
            .document(sessionId)
            .get()
            .addOnSuccessListener { document ->
                val finalMovie = document.getString("finalMovie") ?: ""
                val numUsers = document.getLong("numUsers")?.toInt() ?: 2
                val sessionMovieInteractions = document.get("Movies") as? Map<String, Long> ?: emptyMap()
                numUsersState.value = numUsers
                finalMovieState.value = finalMovie

                if (finalMovieState.value.isEmpty()) {
                    for ((movieId, longValue) in sessionMovieInteractions) {
                        if (longValue.toInt() == numUsersState.value) {
                            finalMovieState.value = movieId
                            break // Exit the loop after finding the first match
                        }
                    }
                }

                // if finalMovie isn't empty, disable fetching/swiping
                if (finalMovie.isNotEmpty()) {
                    fetchingMoviesAllowed.value = false
                } else {
                    triggerFetchMovies.value = true
                }
            }
    }

    LaunchedEffect(triggerFetchMovies.value) {
        if (triggerFetchMovies.value) {
            fetchRandomMovies()
            triggerFetchMovies.value = false
        }
    }


    // update session data after interactions
    fun updateSessionData(movieId: Int, isLiked: Boolean, onComplete: () -> Unit) {
        val sessionRef = db.collection("sessions").document(sessionId)

        db.runTransaction { transaction ->
            val snapshot = transaction.get(sessionRef)

            val usersMapAny = snapshot.get("users") as? Map<String, Any> ?: emptyMap()
            val usersMap: Map<String, List<String>> = usersMapAny.mapValues { entry ->
                val list = (entry.value as? List<*>)?.filterIsInstance<String>() ?: emptyList()
                list
            }

            val userMovies = usersMap[userId]?.toMutableList() ?: mutableListOf()
            userMovies.add(movieId.toString())
            val updatedUsersMap = usersMap + (userId to userMovies)

            if (isLiked) {
                val moviesMapAny = snapshot.get("Movies") as? Map<String, Any> ?: emptyMap()
                val moviesMap = moviesMapAny.mapValues { entry ->
                    (entry.value as? Long)?.toInt() ?: 0
                }
                val movieLikes = moviesMap[movieId.toString()] ?: 0
                val updatedCount = movieLikes + 1
                val updatedMoviesMap = moviesMap + (movieId.toString() to updatedCount)

                transaction.update(sessionRef, mapOf(
                    "users" to updatedUsersMap,
                    "Movies" to updatedMoviesMap
                ))

                val currentNumUsers = numUsersState.value
                if (currentNumUsers != null && updatedCount == currentNumUsers) {
                    transaction.update(sessionRef, "finalMovie", movieId.toString())
                }
            } else {
                transaction.update(sessionRef, mapOf(
                    "users" to updatedUsersMap
                ))
            }
        }.addOnSuccessListener {
            db.collection("sessions").document(sessionId).get().addOnSuccessListener { doc ->
                val fm = doc.getString("finalMovie") ?: ""
                if (fm.isNotEmpty()) {
                    finalMovieState.value = fm
                    fetchingMoviesAllowed.value = false
                }
                onComplete()
            }.addOnFailureListener {
                onComplete()
            }
        }.addOnFailureListener {
            onComplete()
        }
    }



    // if finalMovie is set, just show that movie
    val finalMovieId = finalMovieState.value

    val finalMovieData = remember { mutableStateOf<FirestoreMovieDB?>(null) }
    LaunchedEffect(finalMovieId) {
        if (finalMovieId.isNotEmpty()) {
            val finalMovieInt = finalMovieId.toIntOrNull() // converting strings to int bc im dumb
            if (finalMovieInt != null) {
                db.collection("movies")
                    .whereEqualTo("id", finalMovieInt)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        finalMovieData.value = querySnapshot.documents.firstOrNull()
                            ?.toObject(FirestoreMovieDB::class.java)
                    }
            } else {
                println("DIDNT WORK")
            }
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

    // check if we need to fetch more movies
    LaunchedEffect(i) {
        if (fetchingMoviesAllowed.value && moviesState.value.isNotEmpty()) {
            val remainingMovies = moviesState.value.size - i
            if (remainingMovies <= 2) {
                fetchRandomMovies()
            }
        }
    }

    val onSwipeLeft: () -> Unit = {
        i++
    }

    val onSwipeRight: () -> Unit = {
        liked = liked.toMutableSet().apply { add(i) }
        i++
    }

    val swipeThreshold: Float = 400f
    val sensitivityFactor: Float = 3f

    LaunchedEffect(dismissRight) {
        if (dismissRight && fetchingMoviesAllowed.value) {
            delay(300)
            val currentMovie = moviesState.value.getOrNull(i) ?: return@LaunchedEffect
            updateSessionData(currentMovie.id, true) {
                onSwipeRight.invoke()
                dismissRight = false
            }
        }
    }

    LaunchedEffect(dismissLeft) {
        if (dismissLeft && fetchingMoviesAllowed.value) {
            delay(300)
            val currentMovie = moviesState.value.getOrNull(i) ?: return@LaunchedEffect
            updateSessionData(currentMovie.id, false) {
                onSwipeLeft.invoke()
                dismissLeft = false
            }
        }
    }

    LaunchedEffect(i) {
        if (moviesState.value.isNotEmpty() && i > 0 && i == moviesState.value.size){
            preview = true
        }
    }

    // If finalMovie is set, just show that movie
    if (finalMovieId.isNotEmpty()) {
        val movie = finalMovieData.value
        if (movie == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Loading final movie...")
            }
            return
        } else {
            Box(modifier = Modifier.fillMaxSize()) {

                Content(movie)
                MovieTitleCard(
                    modifier = Modifier.padding(top = 50.dp),
                    movieTitle = "Your session picked a movie!",
                    fontSize = 24
                )
                Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                    MovieTitleCard(
                        modifier = Modifier.padding(top = 50.dp),
                        movieTitle = movie.title,
                        fontSize = 24
                    )
                    MovieTitleCard(
                        movieTitle = movie.description,
                        fontSize = 20
                    )
                }
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
            }
            return
        }
    }

    // do usual stuff if finalMovie is empty
    if (moviesState.value.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
        return
    }

    val currentMovie = moviesState.value.getOrNull(i)
    if (currentMovie == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No more movies")
        }
        return
    }

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
        // get next movie to show behind current one if available
        val nextMovieIndex = (i+1).mod(moviesState.value.size)
        val nextMovie = moviesState.value.getOrElse(nextMovieIndex) { currentMovie }

        Box {
            SubcomposeAsyncImage(
                model = "${base_url}${nextMovie.poster}",
                contentDescription = "Next image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                loading = { CircularProgressIndicator() },
            )
            Box(modifier = Modifier
                .offset { IntOffset(offset.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(onDragEnd = {
                        offset = 0f
                    }) { change, dragAmount ->
                        if (fetchingMoviesAllowed.value) {
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
                            if (fetchingMoviesAllowed.value) dismissRight = true
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
                            if (fetchingMoviesAllowed.value) dismissLeft = true
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
