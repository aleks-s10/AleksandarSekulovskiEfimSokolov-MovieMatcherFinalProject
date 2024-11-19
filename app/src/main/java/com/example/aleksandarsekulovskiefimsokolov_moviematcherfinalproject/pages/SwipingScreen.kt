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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.AuthViewModel
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.R
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.math.roundToInt

val images = arrayOf(
    R.drawable.transformers,
    R.drawable.deadpool,
    R.drawable.mazerunner,
    R.drawable.moana,
    R.drawable.wick,
    R.drawable.venom,
    R.drawable.joker,
    R.drawable.minecraft,
    R.drawable.panda
    )

val titles = arrayOf(
    "Transformers One",
    "Deadpool and Wolverine",
    "Maze Runner: Scorch Trials",
    "Moana",
    "John Wick",
    "Venom",
    "The Joker",
    "The Minecraft Movie",
    "Kung Fu Panda 3"
)
val descriptions = arrayOf(
    "An origin story exploring the beginnings of Optimus Prime and Megatron as friends before becoming enemies in the Transformers universe.",
    "The hilarious and action-packed team-up of Deadpool and Wolverine as they navigate a chaotic mission filled with quips and claws.",
    "A thrilling continuation of the Maze Runner saga where the Gladers face new challenges in a scorching desert full of dangers and secrets.",
    "A daring young girl sets sail across the ocean to save her people and discovers her true destiny with the help of a demigod.",
    "A retired hitman seeks vengeance against those who wronged him, unleashing a relentless wave of action and precision.",
    "A journalist bonds with an alien symbiote, gaining extraordinary powers to fight a dark threat while grappling with his own duality.",
    "A deep and gritty exploration of the origins of the Joker, diving into the psyche of a troubled individual and his transformation into the iconic villain.",
    "An epic adventure in the Minecraft universe where characters must build, craft, and survive to protect their world from a looming threat.",
    "Po and his friends reunite to face a supernatural villain while Po embarks on a journey of self-discovery and learns to master his role as the Dragon Warrior."
)

@Composable
fun Content(i: Int){
    val image = painterResource(images[i.mod(images.size)] )

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = image,
        contentDescription = "Current image"
    )
}

@Composable
fun MovieTitleCard(movieTitle: String, modifier: Modifier = Modifier, fontSize: Int) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
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
            .padding(start = 10.dp, end = 10.dp),
//            .padding(top = 10.dp, start = 15.dp, end = 15.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.LightGray)
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
fun MovieItem(movieIndex: Int, modifier: Modifier = Modifier) {
    val i = movieIndex.mod(images.size)
    Box(
        modifier = modifier
            .padding(8.dp)
            .height(250.dp)
            .aspectRatio(0.75f)
    ) {
        Image(
            painter = painterResource(images[i] ),
            contentDescription = titles[i],
            contentScale = ContentScale.Crop, // Crop the image to fill the aspect ratio
            modifier = Modifier
                .height(250.dp)
                .aspectRatio(0.75f) // Adjust to make the image more square
                .clip(RectangleShape)
        )
        MovieItemCard(
            movieTitle = titles[i],
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.TopCenter),
            fontSize = 15
        )
    }
}
@Composable
fun MovieList(movies: List<Int>) {
    LazyColumn {
        items(movies.chunked(2)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                it.forEach { movieIndex ->
                    MovieItem(movieIndex)
                }
            }
        }
    }
}




@Composable
fun SwipingScreen(modifier : Modifier = Modifier, navController: NavController, authViewModel: AuthViewModel) {
    var offset by remember { mutableStateOf(0f) }
    var dismissRight by remember { mutableStateOf(false) }
    var dismissLeft by remember { mutableStateOf(false) }
    val density = LocalDensity.current.density
    var i by remember { mutableStateOf(0) }
    var liked by remember { mutableStateOf(mutableSetOf<Int>()) }
    var preview by remember { mutableStateOf(false) }

    val onSwipeLeft: () -> Unit = {
        i++
    }
    val onSwipeRight: () -> Unit = {
        liked = liked.toMutableSet().apply { add(i.mod(images.size)) }
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
        if (i > 0 && i.mod(images.size) == 0){
            preview = true
        }
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
            MovieList(liked.toList())

        }
    }
    else {
        Box {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(images[(i + 1).mod(images.size)]),
                contentDescription = "Next image"
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
                Content(i)
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                TextButton(
                    onClick = {
                        preview = true
                    },
                    modifier = Modifier.align(Alignment.TopEnd).padding(top = 15.dp)
                ) { Text(
                    "Preview",
                    fontSize = 20.sp,
                    color = Color.White
                ) }
                Column {
                    MovieTitleCard(
                        modifier = Modifier.padding(top = 50.dp),
                        movieTitle = titles[i.mod(titles.size)],
                        fontSize = 24
                    )
                    MovieTitleCard(
//                    modifier = Modifier.padding(top =  10.dp),
                        movieTitle = descriptions[i.mod(titles.size)],
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
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.LightGray),
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
                        colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.LightGray),
                        onClick = {
                            dismissLeft = true
                        },
                    ) {
                        Image(
                            modifier = Modifier.size(100.dp),
                            painter = painterResource(R.drawable.thumbsdown),
                            contentDescription = "Like",
                        )
                    }
                }
            }
        }
    }
}

