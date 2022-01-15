package com.example.myapplication.viewmodel

import com.example.myapplication.model.Video

sealed class AppState {
    data class Success(val videos: List<Video>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}