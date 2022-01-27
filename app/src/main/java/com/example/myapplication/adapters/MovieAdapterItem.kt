package com.example.myapplication.adapters

import com.example.myapplication.model.MoviePreview


class MovieAdapterItem(
    val moviePreview: MoviePreview,
    val title: String,
    val original_title: String,
    val genres: List<String>,
    val average: String,
    val release_year: String
) :
    AdapterItem(moviePreview.id) {

    override fun equals(other: Any?): Boolean {
        return if (other !is MovieAdapterItem) {
            false
        } else {
            hashCode() == other.hashCode()
        }
    }

    override fun hashCode(): Int {
        return moviePreview.hashCode()
    }
}


