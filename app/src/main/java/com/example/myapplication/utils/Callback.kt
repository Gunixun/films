package com.example.myapplication.utils

interface Callback<T> {

    fun onSuccess(result: T)

    fun onError(err: Throwable)

}