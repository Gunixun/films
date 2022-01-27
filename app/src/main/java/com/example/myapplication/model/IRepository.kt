package com.example.myapplication.model


interface IRepository {

    fun getMovies(callback: Callback<List<MoviePreview>>)

    fun getMovie(movieId: String, callback: Callback<Movie>)
}