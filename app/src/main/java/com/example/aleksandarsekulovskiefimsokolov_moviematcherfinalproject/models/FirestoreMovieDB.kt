package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class FirestoreMovieDB (
    val id: Int,
    val description: String,
    val genre: String,
    val poster: String,
    val rating: Double,
    val release_year: String,
    val title: String,
) {
    constructor() : this(
        id = 0,
        description = "",
        genre = "",
        poster = "",
        rating = 0.0,
        release_year = "",
        title = ""
    )
}