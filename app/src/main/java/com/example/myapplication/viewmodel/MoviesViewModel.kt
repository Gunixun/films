package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.*

class MoviesViewModel(private val movies: MutableLiveData<AppState> = MutableLiveData()) :
    BaseViewModel() {

    private val repository: IRepository = MoviesRepository()

    fun getLiveData(): LiveData<AppState> = movies

    fun getAllMovies() {
        movies.postValue(AppState.Loading)

        repository.getMovies(object : Callback<List<MoviePreview>> {
            override fun onSuccess(result: List<MoviePreview>) {
                movies.postValue(AppState.Success(result))
            }

            override fun onError(err: Throwable) {
                movies.postValue(AppState.Error(err))
            }

        })
    }
}