package com.example.myapplication.repository

import com.example.myapplication.model.Actor
import com.example.myapplication.utils.CallbackData

interface IActorRepository {

    fun getActor(actorId: String, callback: CallbackData<Actor>)
}