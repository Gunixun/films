package com.example.myapplication.viewmodel

import com.example.myapplication.utils.TypeMovies
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.*
import com.example.myapplication.repository.*
import com.example.myapplication.ui.App
import com.example.myapplication.utils.CallbackData

class MoviesViewModel(private val movies: MutableLiveData<AppState> = MutableLiveData()) :
    BaseViewModel() {

    private val repository: IRepository = RetrofitMoviesRepository()
    private val historyRepository: IDBRepository = HistoryRepository(App.getHistoryDao())

    fun getLiveData(): LiveData<AppState> = movies

    fun getMovies(adult: Boolean, movieType: TypeMovies) {
        movies.postValue(AppState.Loading)

        if (movieType == TypeMovies.HISTORY){
            historyRepository.getMovies(object : CallbackData<List<MoviePreview>> {
                override fun onSuccess(result: List<MoviePreview>) {
                    movies.postValue(AppState.Success(result))
                }

                override fun onError(err: Throwable) {
                    movies.postValue(AppState.Error(err))
                }

            })
        } else {
            repository.getMovies(adult, movieType, object : CallbackData<List<MoviePreview>> {
                override fun onSuccess(result: List<MoviePreview>) {
                    movies.postValue(AppState.Success(result))
                }

                override fun onError(err: Throwable) {
                    movies.postValue(AppState.Error(err))
                }

            })
        }
    }
}