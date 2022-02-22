package com.example.myapplication.model.db

import androidx.room.*

@Dao
interface BookmarksDao {
    @Query("SELECT * FROM BookmarksEntity")
    fun getAll(): List<BookmarksEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: BookmarksEntity)

    @Update
    fun update(entity: BookmarksEntity)

    @Delete
    fun delete(entity: BookmarksEntity)
}