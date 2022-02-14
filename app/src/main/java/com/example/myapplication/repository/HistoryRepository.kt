package com.example.myapplication.repository

import android.os.Handler
import android.os.Looper
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.repository.db.HistoryDao
import com.example.myapplication.repository.db.HistoryEntity
import com.example.myapplication.utils.CallbackData
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class HistoryRepository(private val historyDao: HistoryDao) : IDBRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun getMovies(callback: CallbackData<List<MoviePreview>>) {
        executor.execute {
            val movies = historyDao.getAll()
            val moviePreviews: MutableList<MoviePreview> = mutableListOf()
            for (movie in movies) {
                moviePreviews.add(
                    MoviePreview(
                        title = movie.title,
                        original_title = movie.original_title,
                        average = movie.average,
                        genres = mutableListOf(),
                        id = movie.uid.toString(),
                        icon_path = movie.icon_path,
                        release_year = movie.release_year
                    )
                )
            }
            handler.post{callback.onSuccess(moviePreviews)}
        }
    }

    override fun setMovie(movie: Movie) {
        executor.execute {
            historyDao.insert(
                HistoryEntity(
                    uid = movie.id.toInt(),
                    title = movie.title,
                    original_title = movie.original_title,
                    average = movie.average,
                    icon_path = movie.icon_path,
                    release_year = movie.release_year
                )
            )
        }
    }
}