package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movies: List<MovieDB>)

    @Query("SELECT * FROM movies")
    suspend fun getAllMovies(): List<MovieDB>

    @Query("SELECT * FROM movies where page == :page")
    suspend fun getMoviesByPage(page: Int): List<MovieDB>

    @Query("SELECT * FROM movies where favorite == 1")
    suspend fun getFavorites(): List<MovieDB>

    @Query("UPDATE movies SET favorite = 1 WHERE id == :id")
    suspend fun setFavorite(id: String): Unit

    @Query("UPDATE movies SET favorite = 0 WHERE id == :id")
    suspend fun setUnFavorite(id: String): Unit

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(movies: UserDB)

    @Query("SELECT * FROM users where self == 1")
    suspend fun getSelf(): UserDB

    @Query("SELECT * FROM users where pending == 0")
    suspend fun getFriends(): List<UserDB>

    @Query("SELECT * FROM users where pending == 1")
    suspend fun getPending(): List<UserDB>

}
