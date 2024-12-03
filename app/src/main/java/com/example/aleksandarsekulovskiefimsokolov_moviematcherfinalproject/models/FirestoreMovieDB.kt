package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class FirestoreMovieDB (
    val id: String,
    val description: String,
    val genre: String,
    val poster: String,
    val rating: Double,
    val release_year: String,
    val title: String
)