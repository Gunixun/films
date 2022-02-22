package com.example.myapplication.repository

import com.example.myapplication.model.dto.MovieDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieApi {
    @GET("3/movie/{movie_id}")
    fun getMovie(
        @Path("movie_id") movieId: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String = "ru-RU"
    ): Call<MovieDTO>
}