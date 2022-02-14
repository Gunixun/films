package com.example.myapplication.repository

import com.example.myapplication.BuildConfig
import com.example.myapplication.model.Actor
import com.example.myapplication.repository.api.ActorApi
import com.example.myapplication.repository.dto.ActorDTO
import com.example.myapplication.utils.CallbackData
import com.example.myapplication.utils.MAIN_LINK
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitActorRepository : IActorRepository {

    override fun getActor(actorId: String, callback: CallbackData<Actor>) {
        val retrofit = Retrofit.Builder()
            .baseUrl(MAIN_LINK)
            .addConverterFactory(
                GsonConverterFactory.create(GsonBuilder().create())
            )
            .build().create(ActorApi::class.java)
        retrofit.getMovie(actorId, BuildConfig.MOVIE_API_KEY)
            .enqueue(object : Callback<ActorDTO> {
                override fun onResponse(call: Call<ActorDTO>, response: Response<ActorDTO>) {
                    val actorDTO: ActorDTO? = response.body()
                    if (actorDTO != null) {
                        callback.onSuccess(
                            Actor(
                            id = actorDTO.id,
                            name = actorDTO.name,
                            biography = actorDTO.biography,
                            icon_path = actorDTO.profile_path,
                            place_of_birth = actorDTO.place_of_birth,
                            birthday = actorDTO.birthday
                        )
                        )
                    } else {
                        callback.onError(Throwable())
                    }

                }

                override fun onFailure(call: Call<ActorDTO>, e: Throwable) {
                    callback.onError(e)
                }
            })
    }
}