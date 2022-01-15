package com.example.myapplication.adapters

import com.example.myapplication.model.Video

class VideoAdapterItem(
    val video: Video,
    val title: String,
    val genres: String
) :
    AdapterItem(video.id) {

    override fun equals(other: Any?): Boolean {
        return if (other !is VideoAdapterItem) {
            false
        } else {
            hashCode() == other.hashCode()
        }
    }

    override fun hashCode(): Int {
        var result = video.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + genres.hashCode()
        return result
    }
}


