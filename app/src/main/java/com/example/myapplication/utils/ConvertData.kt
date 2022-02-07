package com.example.myapplication.utils

import com.example.myapplication.model.MoviePreview
import com.example.myapplication.model.MoviesDTO

fun convertDTO(adult: Boolean, moviesDTO: MoviesDTO, jenresMovies: MutableMap<Int, String>):
        MutableList<MoviePreview>{

    val moviePreviews: MutableList<MoviePreview> = mutableListOf()
    for (movieDTO in moviesDTO.results) {
        if (movieDTO.adult && !adult) {
            continue
        }
        val genres: MutableList<String> = mutableListOf()
        for (genre in movieDTO.genre_ids) {
            jenresMovies.get(genre)?.let { genres.add(it) }
        }
        moviePreviews.add(
            MoviePreview(
                title = movieDTO.title,
                original_title = movieDTO.original_title,
                average = movieDTO.vote_average.toString(),
                genres = genres,
                id = movieDTO.id,
                icon_path = movieDTO.poster_path,
                release_year = movieDTO.release_date.slice(0..3)
            )
        )
    }
    return moviePreviews
}