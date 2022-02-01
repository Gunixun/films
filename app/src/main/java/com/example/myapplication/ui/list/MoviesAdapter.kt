package com.example.myapplication.ui.list

import DiffUtilsAdapterItems
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.ui.adapters.AdapterItem
import com.example.myapplication.ui.adapters.MovieAdapterItem
import com.example.myapplication.model.MoviePreview


class MoviesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data: MutableList<AdapterItem> = mutableListOf()
    private var onClick: OnClick? = null

    fun getOnClick() = onClick

    fun setOnClick(onClick: OnClick) {
        this.onClick = onClick
    }

    interface OnClick {
        fun onClick(moviePreview: MoviePreview)
        fun onLongClick(moviePreview: MoviePreview)
    }

    fun setData(moviePreviews: List<MoviePreview>) {
        val adapterItems: MutableList<AdapterItem> = mutableListOf()
        for (movie in moviePreviews) {
            adapterItems.add(
                MovieAdapterItem(
                    movie,
                    movie.title,
                    movie.original_title,
                    movie.genres,
                    movie.average,
                    movie.release_year
                )
            )
        }
        val diffResult = DiffUtil.calculateDiff(DiffUtilsAdapterItems(adapterItems, data))
        data.clear()
        data.addAll(adapterItems)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun getItemCount() = data.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MovieViewHolder) {
            holder.bind(data[position] as MovieAdapterItem)
        }
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(movieItem: MovieAdapterItem) {
//            itemView.findViewById<TextView>(R.id.image_view).text = video.title
            with(itemView) {
                findViewById<TextView>(R.id.card_title).text = movieItem.title
                findViewById<TextView>(R.id.card_subtitle).text =
                    "${movieItem.release_year}/${movieItem.original_title}"
                findViewById<TextView>(R.id.genres_textview).text =
                    movieItem.genres.joinToString(separator = ", ")
                findViewById<TextView>(R.id.rating_title).text = movieItem.average
                setOnClickListener {
                    getOnClick()?.onClick(movieItem.moviePreview)
                }
            }
        }
    }
}