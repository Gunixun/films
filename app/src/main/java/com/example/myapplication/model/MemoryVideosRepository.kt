package com.example.myapplication.model

import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.util.*
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class MemoryVideosRepository : IRepository {

    private val executor: Executor = Executors.newSingleThreadExecutor()
    private val handler = Handler(Looper.getMainLooper())

    private val videos: MutableList<Video> = mutableListOf()


    init {
        videos.add(
            Video(
                "Человек паук",
                "Описание",
                "9.0",
                "Триллер",
                UUID.randomUUID().toString()
            )
        )
        videos.add(
            Video(
                "Человек паук2",
                "Описание",
                "9.0",
                "Триллер",
                UUID.randomUUID().toString()
            )
        )
        videos.add(
            Video(
                "Человек паук3",
                "Описание",
                "9.0",
                "Триллер",
                UUID.randomUUID().toString()
            )
        )
    }


    override fun getFilms(callback: Callback<List<Video>>) {
        executor.execute {
            try {
                Thread.sleep(3000L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            handler.post {
                if (Random().nextBoolean()) {
                    if (Random().nextBoolean()) {
                        callback.onSuccess(videos);
                    } else {
                        callback.onSuccess(mutableListOf());
                    }
                } else {
                    callback.onError(IOException());
                }
            }
        }
    }
}