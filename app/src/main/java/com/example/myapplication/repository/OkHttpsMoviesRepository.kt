package com.example.myapplication.repository

import android.os.Handler
import android.os.Looper
import com.example.myapplication.model.*
import com.example.myapplication.utils.*
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException


class OkHttpsMoviesRepository : IRepository {

    private val handler = Handler(Looper.getMainLooper())
    private var jenresMovies: MutableMap<Int, String> = mutableMapOf()

    fun parseGenresMovies() {
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder() // Создаём строителя запроса
        builder.url("${MAIN_LINK}3/genre/movie/list?$API_KEY&$LANGUAGE") // Формируем URL
        val request: Request = builder.build() // Создаём запрос
        client.newCall(request).execute().use { response ->
            val json = JSONObject(response.body()!!.string())
            val array = json.getJSONArray("genres")
            for (i in 0 until array.length()) {
                val genre = array.getJSONObject(i)
                jenresMovies[genre.getInt("id")] = genre.getString("name")
            }
        }
    }


    override fun getMovies(adult: Boolean, callback: CallbackData<List<MoviePreview>>) {
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder() // Создаём строителя запроса
        builder.url("${MAIN_LINK}3/movie/popular?$API_KEY&$LANGUAGE") // Формируем URL
        val request: Request = builder.build() // Создаём запрос
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {

            // Вызывается, если ответ от сервера пришёл
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                val serverResponse: String? = response.body()?.string()
                // Синхронизируем поток с потоком UI
                if (response.isSuccessful && serverResponse != null) {
                    val moviesDTO: MoviesDTO = Gson().fromJson(serverResponse, MoviesDTO::class.java)
                    if (jenresMovies.isEmpty()) {
                        parseGenresMovies()
                    }
                    handler.post {
                        callback.onSuccess(convertDTO(adult, moviesDTO, jenresMovies))
                    }

                } else {
                    handler.post {
                        callback.onError(Throwable())
                    }
                }
            }

            // Вызывается при сбое в процессе запроса на сервер
            override fun onFailure(call: Call?, e: IOException?) {
                handler.post {
                    callback.onError(Throwable(e.toString()))
                }
            }
        })
    }

    override fun getMovie(movieId: String, callback: CallbackData<Movie>) {
        val client = OkHttpClient() // Клиент
        val builder: Request.Builder = Request.Builder() // Создаём строителя запроса
        builder.url("${MAIN_LINK}3/movie/$movieId?$API_KEY&$LANGUAGE") // Формируем URL
        val request: Request = builder.build() // Создаём запрос
        val call: Call = client.newCall(request)
        call.enqueue(object : Callback {

            // Вызывается, если ответ от сервера пришёл
            @Throws(IOException::class)
            override fun onResponse(call: Call?, response: Response) {
                val serverResponse: String? = response.body()?.string()
                // Синхронизируем поток с потоком UI
                if (response.isSuccessful && serverResponse != null) {
                    val movieDTO: MovieDTO =
                        Gson().fromJson(serverResponse, MovieDTO::class.java)

                    if (jenresMovies.isEmpty()) {
                        parseGenresMovies()
                    }

                    val genres: MutableList<String> = mutableListOf()
                    for (genre in movieDTO.genres) {
                        jenresMovies.get(genre.id)?.let { genres.add(it) }
                    }

                    handler.post {
                        callback.onSuccess(
                            Movie(
                                title = movieDTO.title,
                                original_title = movieDTO.original_title,
                                average = movieDTO.vote_average.toString(),
                                genres = genres,
                                id = movieDTO.id,
                                icon_path = movieDTO.poster_path,
                                release_year = movieDTO.release_date.slice(0..3),
                                overview = movieDTO.overview,
                            )
                        )
                    }

                } else {
                    handler.post {
                        callback.onError(Throwable())
                    }
                }
            }

            // Вызывается при сбое в процессе запроса на сервер
            override fun onFailure(call: Call?, e: IOException?) {
                handler.post {
                    callback.onError(Throwable(e.toString()))
                }
            }
        })
    }
}