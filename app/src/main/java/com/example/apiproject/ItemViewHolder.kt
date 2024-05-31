package com.example.apiproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.Item
import com.squareup.picasso.Picasso


class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
    private val viewTextView: TextView = itemView.findViewById(R.id.viewTextView)

    fun bind(item: Item) {
        nameTextView.text = item.name
        priceTextView.text = "Price: ${item.price}"
        viewTextView.text = "Views: ${item.view}"
        val url = "http://192.168.1.5/api/images/${item.img}"
        Picasso.get().load(url).into(imageView)
    }
}
