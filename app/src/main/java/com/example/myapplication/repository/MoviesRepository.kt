package com.example.myapplication.repository

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.myapplication.BuildConfig
import com.example.myapplication.utils.Callback
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MovieDTO
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.model.MoviePreviewDTO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


@RequiresApi(Build.VERSION_CODES.N)
private fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}


class MoviesRepository : IRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private var jenresMovies: MutableMap<Int, String> = mutableMapOf()
    private var jenresTV: Map<Int, String>? = null

    @RequiresApi(Build.VERSION_CODES.N)
    fun parseGenresMovies() {
        val uri =
            URL(
                "https://api.themoviedb.org/3/genre/movie/list" +
                        "?api_key=${BuildConfig.MOVIE_API_KEY}&language=ru-RU"
            )
        lateinit var urlConnection: HttpsURLConnection
        try {
            urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
            urlConnection.readTimeout = 10000
            val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))

            val json = JSONObject(getLines(bufferedReader))
            val array = json.getJSONArray("genres")
            for (i in 0 until array.length()) {
                val genre = array.getJSONObject(i)
                jenresMovies[genre.getInt("id")] = genre.getString("name")
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
    override fun getMovies(callback: Callback<List<MoviePreview>>) {
        executor.execute {
            val uri =
                URL(
                    "https://api.themoviedb.org/3/movie/popular" +
                            "?api_key=${BuildConfig.MOVIE_API_KEY}&language=ru-RU"
                )
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))


                val json = JSONObject(getLines(bufferedReader))

                val typeToken = object : TypeToken<List<MoviePreviewDTO>>() {}.type
                val moviesDTO =
                    Gson().fromJson<List<MoviePreviewDTO>>(json.getString("results"), typeToken)

                if (jenresMovies.isEmpty()) {
                    parseGenresMovies()
                }

                val moviePreviews: MutableList<MoviePreview> = mutableListOf()
                for (movieDTO in moviesDTO) {
                    val genres: MutableList<String> = mutableListOf()
                    for (genre in movieDTO.genre_ids) {
                        jenresMovies.get(genre)?.let { genres.add(it) }
                    }
                    moviePreviews.add(
                        MoviePreview(
                            title = movieDTO.title,
                            original_title = movieDTO.original_title,
                            average = movieDTO.vote_average.toString(),
                            genres = genres,
                            id = movieDTO.id,
                            icon_path = movieDTO.poster_path,
                            release_year = movieDTO.release_date.slice(0..3)
                        )
                    )
                }

                handler.post {
                    callback.onSuccess(moviePreviews)
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
    override fun getMovie(movieId: String, callback: Callback<Movie>) {
        executor.execute {
            val uri =
                URL(
                    "https://api.themoviedb.org/3/movie/$movieId" +
                            "?api_key=${BuildConfig.MOVIE_API_KEY}&language=ru-RU"
                )
            lateinit var urlConnection: HttpsURLConnection
            try {
                urlConnection = uri.openConnection() as HttpsURLConnection
                urlConnection.requestMethod = "GET"
                urlConnection.readTimeout = 10000
                val bufferedReader = BufferedReader(InputStreamReader(urlConnection.inputStream))

                if (jenresMovies.isEmpty()) {
                    parseGenresMovies()
                }

                val movieDTO: MovieDTO =
                    Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)

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