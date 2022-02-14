package com.example.myapplication.repository.dto

data class MovieDTO(
    val title: String,
    val original_title: String,
    val vote_average: Float,
    val genres: List<GenreDTO>,
    val id: String,
    val poster_path: String,
    val release_date: String,
    val overview: String,
    val credits: ActorsPreviewDTO,
)