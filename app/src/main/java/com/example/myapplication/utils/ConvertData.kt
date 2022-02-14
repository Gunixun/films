package com.example.myapplication.utils

import com.example.myapplication.model.ActorPreview
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.repository.dto.MovieDTO
import com.example.myapplication.repository.dto.MoviesDTO

fun convertMoviesDTO(adult: Boolean, moviesDTO: MoviesDTO, jenresMovies: MutableMap<Int, String>):
        MutableList<MoviePreview> {

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


fun convertMovieDTO(movieDTO: MovieDTO): Movie {
    val genres: MutableList<String> = mutableListOf()
    for (genre in movieDTO.genres) {
        genres.add(genre.name)
    }
    val actors: MutableList<ActorPreview> = mutableListOf()
    for (credit in movieDTO.credits.cast) {
        if (credit.profile_path != null){
            actors.add(
                ActorPreview(
                    id = credit.id,
                    name = credit.name,
                    icon_path = credit.profile_path
                )
            )
        }
    }
    return Movie(
        title = movieDTO.title,
        original_title = movieDTO.original_title,
        average = movieDTO.vote_average.toString(),
        genres = genres,
        id = movieDTO.id,
        icon_path = movieDTO.poster_path,
        release_year = movieDTO.release_date.slice(0..3),
        overview = movieDTO.overview,
        actors = actors
    )

}


fun getCatalog(movieType: TypeMovies): String {
    if (movieType == TypeMovies.TOP_RATED) {
        return "top_rated"
    } else if (movieType == TypeMovies.POPULAR) {
        return "popular"
    } else {
        return "now_playing"
    }
}