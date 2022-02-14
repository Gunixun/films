package com.example.myapplication.repository.db

import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity")
    fun getAll(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE uid LIKE :id")
    fun  findById(id: String): HistoryEntity
//
//    @Query("UPDATE HistoryEntity SET `temp` = :temp WHERE id = :id")
//    fun updateQuery(temp: Int, id: Long)
//
//    @Query("DELETE FROM HistoryEntity WHERE city LIKE :city")
//    fun deleteQuery(city: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)
}