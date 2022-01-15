package com.example.myapplication.model

interface Callback <T>{

    fun onSuccess(result: T)

    fun onError(err: Throwable)

}