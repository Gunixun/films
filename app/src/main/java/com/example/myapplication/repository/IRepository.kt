package com.example.myapplication.repository

import com.example.myapplication.utils.CallbackData
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.utils.TypeMovies


interface IRepository {

    fun getMovies(adult: Boolean, movieType: TypeMovies, callback: CallbackData<List<MoviePreview>>)

    fun getMovie(movieId: String, callback: CallbackData<Movie>)
}