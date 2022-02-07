package com.example.myapplication.model.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [BookmarksEntity::class], version = 1, exportSchema = false)
abstract class BookmarksDataBase : RoomDatabase() {
    abstract fun bookmarksDao(): BookmarksDao
}