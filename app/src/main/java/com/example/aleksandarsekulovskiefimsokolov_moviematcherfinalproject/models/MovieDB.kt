package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class MovieDB(
    @PrimaryKey val id: String,
    val description: String,
    val genre: String,
    val poster: String,
    val rating: Double,
    val release_year: String,
    val title: String,
    val page: Int,
    val favorite: Boolean
)

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userID: String,
    val userName: String,
    val profilePicture: Int,
    val movies: String,
    val sessions: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val favoriteGenre: String,
    val pending: Boolean,
    val self: Boolean
)


