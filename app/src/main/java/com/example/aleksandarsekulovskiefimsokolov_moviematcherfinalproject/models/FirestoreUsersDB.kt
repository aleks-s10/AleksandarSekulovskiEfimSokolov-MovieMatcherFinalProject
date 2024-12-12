package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class FirestoreUsersDB (
        val id: String,
        val Movies: List<String>?,
        val Profile_Picture: Int,
        val Sessions: List<String>?,
        val email: String,
        val Username: String,
        val FirstName: String,
        val LastName: String,
        val favGenre: String,
        val Friends: List<String>?,
)