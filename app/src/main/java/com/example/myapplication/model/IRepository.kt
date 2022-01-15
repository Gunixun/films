package com.example.myapplication.model


interface IRepository {

    fun getFilms(callback: Callback<List<Video>>)
}