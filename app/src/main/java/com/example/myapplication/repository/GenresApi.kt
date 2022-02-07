package com.example.myapplication.repository

import com.example.myapplication.model.dto.GenresDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GenresApi {
    @GET("3/genre/movie/list")
    fun getGenres(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "ru-RU"
    ): Call<GenresDTO>
}