package com.example.myapplication.repository

import com.example.myapplication.utils.CallbackData
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview


interface IRepository {

    fun getMovies(adult: Boolean, callback: CallbackData<List<MoviePreview>>)

    fun getMovie(movieId: String, callback: CallbackData<Movie>)
}