package com.example.aleksandarsekulovskiefimsokolov_moviematcherfinalproject.models

data class Movies(
    val page: Int,
    val results: List<MovieAPI>,
    val total_pages: Int,
    val total_results: Int
)