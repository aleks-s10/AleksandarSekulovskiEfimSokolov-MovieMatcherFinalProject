package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.data

import com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models.Movies
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Response


// Constants
const val BASE_URL = "https://api.themoviedb.org/3/"
const val apiKey = "a4a43632b097a28262e8e7673da3866e"
//const val bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhNGE0MzYzMmIwOTdhMjgyNjJlOGU3NjczZGEzODY2ZSIsIm5iZiI6MTczMjc0NDMxOS4yMzIsInN1YiI6IjY3NDc5NDdmMWQzZjFiNTljZjQ4Nzk2ZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.zJVaSbk_jvbP-Mcp-zdtkj8Wfhz9kVJtkXUQCot_uOE"
const val bearerToken = "eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJhNGE0MzYzMmIwOTdhMjgyNjJlOGU3NjczZGEzODY2ZSIsIm5iZiI6MTczMjc0NjgzMC4zNzAxNzE4LCJzdWIiOiI2NzQ3OTQ3ZjFkM2YxYjU5Y2Y0ODc5NmYiLCJzY29wZXMiOlsiYXBpX3JlYWQiXSwidmVyc2lvbiI6MX0.QcaCFPLRF6re6PgrhG67f2W9IwlfmgNR5x3kKriqVZk"

// Retrofit Interface
interface ApiInterface {
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String = "a4a43632b097a28262e8e7673da3866e",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): Response<Movies>
}