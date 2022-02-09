package com.example.myapplication.model

data class Movie(
    val title: String,
    val original_title: String,
    val average: String,
    val genres: List<String>,
    val id: String,
    val icon_path: String,
    val release_year: String,
    val overview: String
)