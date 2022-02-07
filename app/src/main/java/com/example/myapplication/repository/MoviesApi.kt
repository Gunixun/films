package com.example.myapplication.repository

import com.example.myapplication.model.dto.MoviesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesApi {
    @GET("3/movie/{catalog}")
    fun getMovies(
        @Path("catalog") catalog: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String = "ru-RU"
    ): Call<MoviesDTO>
}