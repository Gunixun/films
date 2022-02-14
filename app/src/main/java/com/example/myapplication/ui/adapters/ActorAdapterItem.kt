package com.example.myapplication.ui.adapters

import com.example.myapplication.model.ActorPreview


class ActorAdapterItem(
    val actorPreview: ActorPreview,
    val name: String,
    val icon_path: String,
) : AdapterItem(actorPreview.id) {

    override fun equals(other: Any?): Boolean {
        return if (other !is ActorAdapterItem) {
            false
        } else {
            hashCode() == other.hashCode()
        }
    }

    override fun hashCode(): Int {
        return actorPreview.hashCode()
    }
}