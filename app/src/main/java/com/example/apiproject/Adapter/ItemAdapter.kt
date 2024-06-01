package com.example.apiproject.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.Item
import com.example.apiproject.ItemViewHolder
import com.example.apiproject.R

class ItemAdapter(private var items: MutableList<Any?>, private val context : Context)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    inner class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
    }

    override fun getItemViewType(position: Int): Int {
        return if (items[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
            ItemViewHolder(view, context)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            val item = items[position] as Item
            holder.bind(item)
        } else if (holder is LoadingViewHolder) {
            // Bind loading view if needed
        }
    }



    override fun getItemCount(): Int {
        return items.size
    }

    fun addDataToAdapter(newData: MutableList<Item>) {
        items.addAll(newData as MutableList<Any?>)
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        items.add(null)
        notifyItemInserted(items.size - 1)
    }

    fun removeLoadingFooter() {
        val position = items.size - 1
        if (position >= 0 && items[position] == null) {
            items.removeAt(position)
            notifyItemRemoved(position)
        }
    }
    fun setResourceAdapter(res : MutableList<Any?>) {
        items = res
    }
}