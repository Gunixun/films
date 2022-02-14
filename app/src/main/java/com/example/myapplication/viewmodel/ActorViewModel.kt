package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.model.Actor
import com.example.myapplication.repository.IActorRepository
import com.example.myapplication.repository.RetrofitActorRepository
import com.example.myapplication.utils.CallbackData


class ActorViewModel(private val actor: MutableLiveData<AppStateActor> = MutableLiveData()) :
    BaseViewModel() {

    private val repository: IActorRepository = RetrofitActorRepository()

    fun getLiveData(): LiveData<AppStateActor> = actor

    fun getActor(actorId: String) {
        actor.postValue(AppStateActor.Loading)
        repository.getActor(actorId, object : CallbackData<Actor> {
            override fun onSuccess(result: Actor) {
                actor.postValue(AppStateActor.Success(result))
            }

            override fun onError(err: Throwable) {
                actor.postValue(AppStateActor.Error(err))
            }

        })
    }
}