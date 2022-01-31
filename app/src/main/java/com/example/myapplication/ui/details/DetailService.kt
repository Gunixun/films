package com.example.myapplication.ui.details

import android.app.IntentService
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.myapplication.BuildConfig
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MovieDTO
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection


@RequiresApi(Build.VERSION_CODES.N)
private fun getLines(reader: BufferedReader): String {
    return reader.lines().collect(Collectors.joining("\n"))
}


class DetailService(name: String = "DetailService") : IntentService(name) {

    private val broadcastIntent = Intent(DETAILS_INTENT_FILTER)

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onHandleIntent(intent: Intent?) {
        if (intent == null) {
            onEmptyIntent()
        } else {
            val movieId = intent.getStringExtra(MOVIE_ID_EXTRA)
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

                val movieDTO: MovieDTO =
                    Gson().fromJson(getLines(bufferedReader), MovieDTO::class.java)

                val genres: MutableList<String> = mutableListOf()
                for (genre in movieDTO.genres) {
                    genres.add(genre.name)
                }

                onSuccessResponse(
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
            } catch (e: Exception) {
                onErrorRequest(e.message ?: "Empty error")
            } finally {
                urlConnection.disconnect()
            }
        }
    }

    private fun onEmptyIntent() {
        putLoadResult(DETAILS_INTENT_EMPTY_EXTRA)
        sendBroadcast(broadcastIntent)
    }

    private fun onSuccessResponse(movie: Movie) {
        putLoadResult(DETAILS_RESPONSE_SUCCESS_EXTRA)
        broadcastIntent.putExtra(DETAILS_MOVIE_EXTRA, movie)
        sendBroadcast(broadcastIntent)
    }

    private fun putLoadResult(result: String) {
        broadcastIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, result)
    }

    private fun onErrorRequest(error: String) {
        putLoadResult(DETAILS_REQUEST_ERROR_EXTRA)
        broadcastIntent.putExtra(DETAILS_REQUEST_ERROR_MESSAGE_EXTRA, error)
        sendBroadcast(broadcastIntent)
    }
}