package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.*
import com.example.myapplication.repository.*
import com.example.myapplication.ui.App
import com.example.myapplication.utils.CallbackData

class MovieViewModel(private val movie: MutableLiveData<AppStateMovie> = MutableLiveData()) :
    BaseViewModel() {

    private val repository: IRepository = RetrofitMoviesRepository()
    private val historyRepository: IDBRepository = HistoryRepository(App.getHistoryDao())

    fun getLiveData(): LiveData<AppStateMovie> = movie

    fun saveHistory(movie: Movie){
        historyRepository.setMovie(movie)
    }

    fun getMovie(movieId: String) {
        movie.postValue(AppStateMovie.Loading)

        repository.getMovie(movieId, object : CallbackData<Movie> {
            override fun onSuccess(result: Movie) {
                movie.postValue(AppStateMovie.Success(result))
            }

            override fun onError(err: Throwable) {
                movie.postValue(AppStateMovie.Error(err))
            }

        })
    }
}