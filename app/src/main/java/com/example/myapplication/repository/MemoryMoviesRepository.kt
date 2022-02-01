package com.example.myapplication.repository

import android.os.Handler
import android.os.Looper
import com.example.myapplication.utils.CallbackData
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MemoryMoviesRepository : IRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private val videos: MutableList<MoviePreview> = mutableListOf()


    init {
        videos.add(
            MoviePreview(
                "Человек паук: Нет пути домой",
                "Spider man",
                "9.0",
                mutableListOf("боевик", "приключения", "фэнтези", "фантастика"),
                UUID.randomUUID().toString(),
                "",
                "2021"
            )
        )
        videos.add(
            MoviePreview(
                "Человек паук",
                "Spider man",
                "9.0",
                mutableListOf("Ужасы"),
                UUID.randomUUID().toString(),
                "",
                "2021"
            )
        )
        videos.add(
            MoviePreview(
                "Человек паук",
                "Spider man",
                "9.0",
                mutableListOf("Ужасы"),
                UUID.randomUUID().toString(),
                "",
                "2021"
            )
        )
    }


    override fun getMovies(callback: CallbackData<List<MoviePreview>>) {
        executor.execute {
            try {
                Thread.sleep(1000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                callback.onSuccess(videos)
//                if (Random().nextBoolean()) {
//                    if (Random().nextBoolean()) {
//                        callback.onSuccess(videos);
//                    } else {
//                        callback.onSuccess(mutableListOf());
//                    }
//                } else {
//                    callback.onError(IOException());
//                }
            }
        }
    }

    override fun getMovie(movieId: String, callback: CallbackData<Movie>) {
        TODO("Not yet implemented")
    }
}