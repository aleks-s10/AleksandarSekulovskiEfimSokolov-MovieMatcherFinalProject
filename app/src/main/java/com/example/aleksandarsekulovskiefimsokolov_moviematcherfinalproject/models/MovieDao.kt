package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieDB>)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieDB>

    @Query("SELECT * FROM movies where page == :page")
    suspend fun getMoviesByPage(page: Int): List<MovieDB>
}
