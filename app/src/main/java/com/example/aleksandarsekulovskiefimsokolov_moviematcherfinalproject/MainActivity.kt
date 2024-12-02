package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.Movies
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.ui.theme.AleksandarSekulovskiEfimSokolovMovieMatcherFinalProjectTheme
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.RetrofitInstance
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.HttpException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import android.util.Log
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieAPI
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.google.firebase.firestore.firestore

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        analytics = Firebase.analytics
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authViewModel: AuthViewModel by viewModels()
        val db = Firebase.firestore
        setContent {
            AleksandarSekulovskiEfimSokolovMovieMatcherFinalProjectTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MyAppNavigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )

                    val context = LocalContext.current
                    val scope = rememberCoroutineScope()

                    fun translateGenres(genreIds: List<Int>): String {
                        val genreMap = mapOf(
                            28 to "Action",
                            16 to "Animated",
                            99 to "Documentary",
                            18 to "Drama",
                            10751 to "Family",
                            14 to "Fantasy",
                            36 to "History",
                            35 to "Comedy",
                            10752 to "War",
                            80 to "Crime",
                            10402 to "Music",
                            9648 to "Mystery",
                            10749 to "Romance",
                            878 to "Sci-Fi",
                            27 to "Horror",
                            10770 to "TV Movie",
                            53 to "Thriller",
                            37 to "Western",
                            12 to "Adventure"
                        )

                        return genreIds.mapNotNull { genreMap[it] }.joinToString(", ")
                    }

                    // Fetch data when the composable is loaded
                    LaunchedEffect(key1 = true) {
                        println("GOT HERE")
                        fun saveMoviesToFirestore(movies: List<MovieDB>) {
                            movies.forEach { movie ->
                                val data = hashMapOf(
                                    "id" to movie.id,
                                    "description" to movie.description,
                                    "genre" to movie.genre,
                                    "poster" to movie.poster,
                                    "rating" to movie.rating,
                                    "release_year" to movie.release_year,
                                    "title" to movie.title
                                )

                                db.collection("movies")
                                    .add(data)
                                    .addOnSuccessListener { documentReference ->
                                        Log.d("FIRESTORE", "DocumentSnapshot written with ID: ${documentReference.id}")
                                    }
                                    .addOnFailureListener { e ->
                                        Log.w("FIRESTORE", "Error adding document", e)
                                    }
                            }
                        }
                        scope.launch(Dispatchers.IO) {
                            val response = try {
                                println("GOT HERE")
                                RetrofitInstance.api.getPopularMovies()
                            } catch (e: HttpException) {
                                println("GOT HERE EXCEPTION: ${e.message}")
                                // Handle HTTP exception on the main thread
                                launch(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "HTTP error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@launch
                            } catch (e: IOException) {
                                println("GOT HERE EXCEPTION: ${e.message}")

                                // Handle IO exception on the main thread
                                launch(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "IO Exception error: ${e.message}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                return@launch
                            }
                            catch (e: Exception) {
                                println("GOT HERE EXCEPTION: ${e.message}")
                                launch(Dispatchers.Main) {
                                    Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                                return@launch
                            }
                            println("GOT HERE2")
                            if (response.isSuccessful) {
                                println("GOT HERE SUCCESS")
                                val movies = response.body()
                                println(movies)
                                Log.d("RAW_RESPONSE", response.raw().toString())
                                println(response.body())
                                val moviesAPIList: List<MovieAPI> = response.body()?.results ?: emptyList()
                                val moviesList = moviesAPIList.map { movieAPI ->
                                    MovieDB(
                                        id = movieAPI.id.toString(),
                                        description = movieAPI.overview ?: "No description available",
                                        genre = translateGenres(movieAPI.genre_ids ?: emptyList()),
                                        poster = movieAPI.poster_path ?: "",
                                        rating = movieAPI.vote_average,
                                        release_year = movieAPI.release_date?.split("-")?.get(0) ?: "Unknown",
                                        title = movieAPI.title ?: "Untitled"
                                    )
                                }

                                saveMoviesToFirestore(moviesList)
                                launch(Dispatchers.Main) {
                                    // Update UI or show success message
                                    Toast.makeText(
                                        context,
                                        "Get request successful",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.d("API_RESPONSE", "Movies: $movies")// Debug: Print the response data
                                }
                            } else {
                                println("GOT HERE FAILURE")
                                launch(Dispatchers.Main) {
                                    Toast.makeText(
                                        context,
                                        "Failed: ${response.message()}",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                Log.d("API_RESPONSE", "FAILED: ${response.message()}")
                            }
                            println("GOT HERE3")

                        }

                }
                }
            }
        }
    }
}