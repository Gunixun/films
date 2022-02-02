package com.example.myapplication.utils

interface CallbackData<T> {

    fun onSuccess(result: T)

    fun onError(err: Throwable)

}