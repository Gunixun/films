package com.example.myapplication.repository

import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.utils.CallbackData

interface IDBRepository {
    fun getMovies(callback: CallbackData<List<MoviePreview>>)

    fun setMovie(movie: Movie)
}