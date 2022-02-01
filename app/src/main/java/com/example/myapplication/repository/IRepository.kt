package com.example.myapplication.repository

import com.example.myapplication.utils.Callback
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview


interface IRepository {

    fun getMovies(callback: Callback<List<MoviePreview>>)

    fun getMovie(movieId: String, callback: Callback<Movie>)
}