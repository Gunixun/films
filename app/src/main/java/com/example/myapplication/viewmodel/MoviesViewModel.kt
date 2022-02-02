package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.*
import com.example.myapplication.repository.IRepository
import com.example.myapplication.repository.OkHttpsMoviesRepository
import com.example.myapplication.repository.RetrofitMoviesRepository
import com.example.myapplication.utils.CallbackData

class MoviesViewModel(private val movies: MutableLiveData<AppState> = MutableLiveData()) :
    BaseViewModel() {

    private val repository: IRepository = RetrofitMoviesRepository()

    fun getLiveData(): LiveData<AppState> = movies

    fun getAllMovies() {
        movies.postValue(AppState.Loading)

        repository.getMovies(object : CallbackData<List<MoviePreview>> {
            override fun onSuccess(result: List<MoviePreview>) {
                movies.postValue(AppState.Success(result))
            }

            override fun onError(err: Throwable) {
                movies.postValue(AppState.Error(err))
            }

        })
    }
}