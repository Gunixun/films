package com.example.myapplication.ui.details

import DiffUtilsAdapterItems
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.model.ActorPreview
import com.example.myapplication.ui.adapters.AdapterItem
import com.example.myapplication.ui.adapters.MovieAdapterItem
import com.example.myapplication.model.MoviePreview
import com.example.myapplication.ui.adapters.ActorAdapterItem
import com.example.myapplication.utils.MAIN_POSTER_LINK
import com.example.myapplication.utils.POSTER_SIZE


class ActorsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data: MutableList<AdapterItem> = mutableListOf()
    private var onClick: OnClick? = null

    fun getOnClick() = onClick

    fun setOnClick(onClick: OnClick) {
        this.onClick = onClick
    }

    interface OnClick {
        fun onClick(actorPreview: ActorPreview)
        fun onLongClick(actorPreview: ActorPreview)
    }

    fun setData(actorPreviews: List<ActorPreview>) {
        val adapterItems: MutableList<AdapterItem> = mutableListOf()
        for (actor in actorPreviews) {
            adapterItems.add(
                ActorAdapterItem(
                    actor,
                    actor.name,
                    actor.icon_path
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
        return ActorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_actor, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ActorViewHolder) {
            holder.bind(data[position] as ActorAdapterItem)
        }
    }

    inner class ActorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(actorItem: ActorAdapterItem) {
            with(itemView) {

                Glide
                    .with(itemView)
                    .load("$MAIN_POSTER_LINK$POSTER_SIZE${actorItem.icon_path}")
                    .into(findViewById<ImageView>(R.id.actor_icon));
                findViewById<TextView>(R.id.name_actor_text_view).text = actorItem.name
                setOnClickListener {
                    getOnClick()?.onClick(actorItem.actorPreview)
                }
            }
        }
    }
}