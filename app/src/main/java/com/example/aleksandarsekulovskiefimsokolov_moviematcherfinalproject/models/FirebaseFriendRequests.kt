package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class FirebaseFriendRequests (
    val receiver: String = "",
    val senders: List<String> = emptyList(),
)