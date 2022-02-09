package com.example.myapplication.ui.contacts

import DiffUtilsAdapterItems
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.model.Contact
import com.example.myapplication.ui.adapters.AdapterItem
import com.example.myapplication.ui.adapters.ContactAdapterItem


class ContactsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val data: MutableList<AdapterItem> = mutableListOf()

    fun setData(contacts: List<Contact>) {
        val adapterItems: MutableList<AdapterItem> = mutableListOf()
        for (contact in contacts) {
            adapterItems.add(
                ContactAdapterItem(
                    id = contact.id,
                    name = contact.name
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
        return ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ContactViewHolder) {
            holder.bind(data[position] as ContactAdapterItem)
        }
    }

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(contactItem: ContactAdapterItem) {
            with(itemView) {
                findViewById<TextView>(R.id.name_contact_text_view).text = contactItem.name
            }
        }
    }
}