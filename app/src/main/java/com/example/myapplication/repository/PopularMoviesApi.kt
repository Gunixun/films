package com.example.myapplication.repository

import com.example.myapplication.model.MoviesDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PopularMoviesApi {
    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") api_key: String,
        @Query("language") language: String = "ru-RU"
    ): Call<MoviesDTO>
}