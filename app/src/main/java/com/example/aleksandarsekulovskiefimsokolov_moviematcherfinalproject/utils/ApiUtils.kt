package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.room.Room
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieAPI
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDatabase
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils.translateGenres
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

object DatabaseProvider {
    private var database: MovieDatabase? = null

    fun getDatabase(context: Context): MovieDatabase {
        if (database == null) {
            synchronized(MovieDatabase::class) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database"
                    ).build()
                }
            }
        }
        return database!!
    }
}

suspend fun fetchAndStoreMovies(context: Context, page: Int) {
    val db = DatabaseProvider.getDatabase(context)

    try {
        val response = RetrofitInstance.api.getPopularMovies(page = page)
        if (response.isSuccessful) {
            val moviesAPIList: List<MovieAPI> = response.body()?.results ?: emptyList()
            val moviesList = moviesAPIList.mapIndexed {index, movieAPI ->
                MovieDB(
                    id = movieAPI.id.toString(),
                    description = movieAPI.overview ?: "No description available",
                    genre = translateGenres(movieAPI.genre_ids ?: emptyList()),
                    poster = movieAPI.poster_path ?: "",
                    rating = movieAPI.vote_average,
                    release_year = movieAPI.release_date?.split("-")?.get(0) ?: "Unknown",
                    title = movieAPI.title ?: "Untitled",
                    page = page,
                    favorite = false,
                )

            }
            db.movieDao().insertAll(moviesList)
        } else {
            withContext(Dispatchers.Main) {
                println("PAGE NUMBER IS:$page")
                println("API Error:\n ${response.errorBody()?.string()}")
                println("API Error:\\n ${response.errorBody()}")
                Toast.makeText(context, "API Error:\n ${response.errorBody()}", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: IOException) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

suspend fun saveMoviesToFirestore(context: Context, page: Int) {

    val db = FirebaseFirestore.getInstance()
    var start = (page * 20 - 20) + 1
    var moviesList = emptyList<MovieDB>()

    val response = RetrofitInstance.api.getPopularMovies(page = page)
    if (response.isSuccessful) {
        val moviesAPIList: List<MovieAPI> = response.body()?.results ?: emptyList()
        moviesList = moviesAPIList.mapIndexed { index, movieAPI ->
            MovieDB(
                id = (index + start).toString(),
                description = movieAPI.overview ?: "No description available",
                genre = translateGenres(movieAPI.genre_ids ?: emptyList()),
                poster = movieAPI.poster_path ?: "",
                rating = movieAPI.vote_average,
                release_year = movieAPI.release_date?.split("-")?.get(0) ?: "Unknown",
                title = movieAPI.title ?: "Untitled",
                page = page,
                favorite = false,
            )
            }

        moviesList.forEach { movie ->
        val data = hashMapOf(
            "id" to movie.id.toInt(),
            "description" to movie.description,
            "genre" to movie.genre,
            "poster" to movie.poster,
            "rating" to movie.rating,
            "release_year" to movie.release_year,
            "title" to movie.title
        )

        db.collection("movies").document(movie.id).set(data)
            .addOnSuccessListener { documentReference ->
                Log.d(
                    "FIRESTORE",
                    "DocumentSnapshot written with ID: ${movie.id}"
                )
            }
            .addOnFailureListener { e ->
                Log.w("FIRESTORE", "Error adding document", e)

        } }
    } else {
            withContext(Dispatchers.Main) {
                println("PAGE NUMBER IS:$page")
                println("API Error:\n ${response.errorBody()?.string()}")
                println("API Error:\\n ${response.errorBody()}")
                Toast.makeText(context, "API Error:\n ${response.errorBody()}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}

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


