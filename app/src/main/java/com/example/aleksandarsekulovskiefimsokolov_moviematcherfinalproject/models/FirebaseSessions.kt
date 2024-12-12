package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class FirebaseSessions (
    val sessionID: String = "",
    val users: Map<String,List<String>>,
    val movies: Map<String,List<Int>>,
)
