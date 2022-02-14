package com.example.myapplication.repository.api

import com.example.myapplication.repository.dto.ActorDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ActorApi {
    @GET("3/person/{person_id}")
    fun getMovie(
        @Path("person_id") actorId: String,
        @Query("api_key") api_key: String,
        @Query("language") language: String = "ru-RU"
    ): Call<ActorDTO>
}