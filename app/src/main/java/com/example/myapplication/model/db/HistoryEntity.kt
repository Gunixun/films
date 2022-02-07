package com.example.myapplication.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class HistoryEntity(
    @PrimaryKey
    val uid: Int,
    @ColumnInfo(name = "TITLE")
    val title: String,
    @ColumnInfo(name = "ORIGINAL_TITLE")
    val original_title: String,
    @ColumnInfo(name = "AVERAGE")
    val average: String,
    @ColumnInfo(name = "ICON_PATH")
    val icon_path: String,
    @ColumnInfo(name = "RELEASE_YEAR")
    val release_year: String
)