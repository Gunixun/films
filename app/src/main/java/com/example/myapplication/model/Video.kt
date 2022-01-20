package com.example.myapplication.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Video(
    val title: String,
    val body: String,
    val rating: String,
    val genres: String,
    val id: String
) : Parcelable
