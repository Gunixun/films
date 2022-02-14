package com.example.myapplication.repository

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.model.*
import com.example.myapplication.repository.dto.GenresDTO
import com.example.myapplication.repository.dto.MovieDTO
import com.example.myapplication.repository.dto.MoviesDTO
import com.example.myapplication.utils.*
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


class MoviesRepository : IRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private var jenresMovies: MutableMap<Int, String> = mutableMapOf()

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun parseGenresMovies() {
        val uri = URL("${MAIN_LINK}3/genre/movie/list?$API_KEY&$LANGUAGE")
        lateinit var urlConnection: HttpsURLConnection
        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 10000
            val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))

            val genresDTO: GenresDTO =
                Gson().fromJson(getLines(bufferedReader), GenresDTO::class.java)
            for (genreDTO in genresDTO.genres) {
                jenresMovies[genreDTO.id] = genreDTO.name
            }

        } catch (e: Exception) {
            Log.e("", "Fail connection", e)
            e.printStackTrace()
            //Обработка ошибки
        } finally {
            urlConnection.disconnect()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovies(
        adult: Boolean,
        movieType: TypeMovies,
        callback: CallbackData<List<MoviePreview>>
    ) {
        executor.execute {
            val uri = URL("${MAIN_LINK}3/movie/popular?$API_KEY&$LANGUAGE&append_to_response=credits")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))

                val moviesDTO: MoviesDTO =
                    Gson().fromJson(getLines(bufferedReader), MoviesDTO::class.java)

                if (jenresMovies.isEmpty()) {
                    parseGenresMovies()
                }

                handler.post {
                    callback.onSuccess(convertMoviesDTO(adult, moviesDTO, jenresMovies))
                }
            } catch (e: Exception) {
                Log.e("", "Fail connection", e)
                e.printStackTrace()
                handler.post {
                    callback.onError(e)
                }
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun getMovie(movieId: String, callback: CallbackData<Movie>) {
        executor.execute {
            val uri = URL("${MAIN_LINK}3/movie/$movieId?$API_KEY&$LANGUAGE")
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))

                val movieDTO: MovieDTO =
                    Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)
                val movie = convertMovieDTO(movieDTO)

                handler.post { callback.onSuccess(movie) }
            } catch (e: Exception) {
                Log.e("", "Fail connection", e)
                e.printStackTrace()
                handler.post {
                    callback.onError(e)
                }
            } finally {
                urlConnection.disconnect()
            }
        }
    }

}