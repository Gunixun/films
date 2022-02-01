package com.example.myapplication.model

data class MoviePreviewDTO(
    val title: String,
    val original_title: String,
    val vote_average: Float,
    val genre_ids: List<Int>,
    val id: String,
    val poster_path: String,
    val release_date: String
)