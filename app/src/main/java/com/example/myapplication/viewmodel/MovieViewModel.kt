package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.*

class MovieViewModel(private val movie: MutableLiveData<AppStateMovie> = MutableLiveData()) :
    BaseViewModel() {

    private val repository: IRepository = MoviesRepository()

    fun getLiveData(): LiveData<AppStateMovie> = movie

    fun getMovie(moviePreview: MoviePreview) {
        movie.postValue(AppStateMovie.Loading)

        repository.getMovie(moviePreview.id, object : Callback<Movie> {
            override fun onSuccess(result: Movie) {
                movie.postValue(AppStateMovie.Success(result))
            }

            override fun onError(err: Throwable) {
                movie.postValue(AppStateMovie.Error(err))
            }

        })
    }
}