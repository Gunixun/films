package com.example.myapplication.ui

import android.app.Application
import androidx.room.Room
import com.example.myapplication.repository.db.BookmarksDao
import com.example.myapplication.repository.db.BookmarksDataBase
import com.example.myapplication.repository.db.HistoryDao
import com.example.myapplication.repository.db.HistoryDataBase


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }
    companion object {
        private var instance: App? = null
        private var db: HistoryDataBase? = null
        private var bookmarksDB: BookmarksDataBase? = null


        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                synchronized(HistoryDataBase::class.java) {
                    if (db == null) {
                        if (instance == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(
                            instance!!.applicationContext,
                            HistoryDataBase::class.java,
                            "History.db"
                        )
                            .fallbackToDestructiveMigration() // позволяет не настраивать миграцию, при изменении БД будет пересоздавтаься с потерей данных
                            .build()
                    }
                }
            }

            return db!!.historyDao()
        }

        fun getBookmarksDao(): BookmarksDao {
            if (bookmarksDB == null) {
                synchronized(BookmarksDataBase::class.java) {
                    if (bookmarksDB == null) {
                        if (instance == null) throw IllegalStateException("Application is null while creating DataBase")
                        bookmarksDB = Room.databaseBuilder(
                            instance!!.applicationContext,
                            BookmarksDataBase::class.java,
                            "Bookmarks.db"
                        )
                            .fallbackToDestructiveMigration() // позволяет не настраивать миграцию, при изменении БД будет пересоздавтаься с потерей данных
                            .build()
                    }
                }
            }

            return bookmarksDB!!.bookmarksDao()
        }
    }
}