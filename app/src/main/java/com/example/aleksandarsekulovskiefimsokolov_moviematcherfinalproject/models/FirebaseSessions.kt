package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class FirebaseSessions (
    val sessionID: String = "",
    val users: Map<String,List<String>>,
    val movies: Map<String,List<Int>>,
    val numUsers: Int = 0,
    val finalMovie: String = "",
    val sessionName: String = "",
)
