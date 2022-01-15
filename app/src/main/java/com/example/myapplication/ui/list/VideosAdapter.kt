package com.example.myapplication.ui.list

import DiffUtilsAdapterItems
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapters.AdapterItem
import com.example.myapplication.adapters.VideoAdapterItem
import com.example.myapplication.model.Video


class VideosAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data: MutableList<AdapterItem> = mutableListOf()
    private var onClick: OnClick? = null

    fun getOnClick(): OnClick? {
        return onClick
    }

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
                    video.genres
                )
            )
        }
        val diffResult = DiffUtil.calculateDiff(DiffUtilsAdapterItems(adapterItems, data))
        data.clear()
        data.addAll(adapterItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return VideoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VideoViewHolder) {
            val noteViewHolder = holder
            val video: VideoAdapterItem = data[position] as VideoAdapterItem
            noteViewHolder.title.text = video.title
            noteViewHolder.genres.text = video.genres
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class VideoViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView
        val title: TextView
        val genres: TextView

        init {
            image = itemView.findViewById(R.id.image_view)
            title = itemView.findViewById(R.id.card_title)
            genres = itemView.findViewById(R.id.card_subtitle)

            val card: CardView = itemView.findViewById(R.id.card)
            card.setOnClickListener {
                val item = data[adapterPosition]
                if (item is VideoAdapterItem) {
                    if (getOnClick() != null) {
                        getOnClick()?.onClick(item.video)
                    }
                }
            }

        }
    }
}