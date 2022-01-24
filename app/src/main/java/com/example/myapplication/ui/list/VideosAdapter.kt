package com.example.myapplication.ui.list

import DiffUtilsAdapterItems
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.AdapterItem
import com.example.myapplication.adapters.VideoAdapterItem
import com.example.myapplication.model.Video


class VideosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data: MutableList<AdapterItem> = mutableListOf()
    private var onClick: OnClick? = null

    fun getOnClick() = onClick

    fun setOnClick(onClick: OnClick) {
        this.onClick = onClick
    }

    interface OnClick {
        fun onClick(video: Video)
        fun onLongClick(video: Video)
    }

    fun setData(videos: List<Video>) {
        val adapterItems: MutableList<AdapterItem> = mutableListOf()
        for (video in videos) {
            adapterItems.add(
                VideoAdapterItem(
                    video,
                    video.title,
                    video.genres,
                    video.rating
                )
            )
        }
        val diffResult = DiffUtil.calculateDiff(DiffUtilsAdapterItems(adapterItems, data))
        data.clear()
        data.addAll(adapterItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoViewHolder) {
            holder.bind(data[position] as VideoAdapterItem)
        }
    }

    override fun getItemCount() = data.size

    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(videoItem: VideoAdapterItem) {
//            itemView.findViewById<TextView>(R.id.image_view).text = video.title
            with(itemView) {
                findViewById<TextView>(R.id.card_title).text = videoItem.title
                findViewById<TextView>(R.id.genres_textview).text = videoItem.genres
                findViewById<TextView>(R.id.rating_title).text = "Рейтинг: ${videoItem.rating}"
                setOnClickListener {
                    if (getOnClick() != null) {
                        getOnClick()?.onClick(videoItem.video)
                    }
                }
            }
        }
    }
}