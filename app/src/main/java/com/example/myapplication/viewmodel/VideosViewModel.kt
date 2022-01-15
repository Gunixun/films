package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.Callback
import com.example.myapplication.model.IRepository
import com.example.myapplication.model.MemoryVideosRepository
import com.example.myapplication.model.Video

class VideosViewModel(private val videos : MutableLiveData<AppState> = MutableLiveData()): ViewModel(){

    private val repository: IRepository = MemoryVideosRepository()

    fun getLiveData() : LiveData<AppState> = videos

    fun getAllVideos() {
        videos.postValue(AppState.Loading)

        repository.getFilms(object: Callback<List<Video>> {
            override fun onSuccess(result: List<Video>){
                videos.postValue(AppState.Success(result))
            }

            override fun onError(err: Throwable) {
                videos.postValue(AppState.Error(err))
            }

        })

    }
}