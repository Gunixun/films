package com.example.myapplication.repository

import android.os.Handler
import android.os.Looper
import com.example.myapplication.model.Movie
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.repository.db.BookmarksDao
import com.example.myapplication.repository.db.BookmarksEntity
import com.example.myapplication.utils.CallbackData
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class BookmarksRepository(private val bookmarks: BookmarksDao) : IDBRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    override fun getMovies(callback: CallbackData<List<MoviePreview>>) {
        executor.execute {
            val movies = bookmarks.getAll()
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
            bookmarks.insert(
                BookmarksEntity(
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