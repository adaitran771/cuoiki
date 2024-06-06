package com.example.apiproject.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.Item
import com.example.apiproject.PendingFragment
import com.example.apiproject.R
import com.example.apiproject.order.Order

class ItemOrderAdapter(private var items: MutableList<Any?>, private val context : Context, private val status: Int)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.d("aaaa", "gggggg")
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
            return ItemOrderViewHolder(view, context)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            Log.d("gggggg", "gggggg")
            val item = items[position] as Order
            if(holder is ItemOrderViewHolder) {
                Log.d("gggggg", "gggggg")

                holder.bind(item, status)
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