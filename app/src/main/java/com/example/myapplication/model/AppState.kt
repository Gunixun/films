package com.example.myapplication.viewmodel

import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview

sealed class AppState {
    data class Success(val movies: List<MoviePreview>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}


sealed class AppStateMovie {
    data class Success(val movie: Movie) : AppStateMovie()
    data class Error(val error: Throwable) : AppStateMovie()
    object Loading : AppStateMovie()
}