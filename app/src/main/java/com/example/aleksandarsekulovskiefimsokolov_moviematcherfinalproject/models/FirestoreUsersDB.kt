package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class FirestoreUsersDB (
        val id: String,
        val Movies: List<String>?,
        val Profile_Picture: String,
        val Sessions: List<String>?,
        val email: String,
        val username: String,
        val FirstName: String,
        val LastName: String,
        val favGenre: String
)