package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.utils

import android.content.Context
import android.widget.Toast
import androidx.room.Room
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieAPI
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDB
import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.MovieDatabase
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
            db.movieDao().insertAll(moviesList)
        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "API Error: ${response.message()}", Toast.LENGTH_SHORT).show()
            }
        }
    } catch (e: IOException) {
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "Network Error: ${e.message}", Toast.LENGTH_SHORT).show()
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
