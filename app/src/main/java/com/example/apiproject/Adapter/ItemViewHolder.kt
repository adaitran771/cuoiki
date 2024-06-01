package com.example.apiproject.Adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.Item
import com.example.apiproject.R
import com.example.apiproject.detail_product
import com.squareup.picasso.Picasso


class ItemViewHolder(itemView: View,private val context: Context) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imageView)
    private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
    private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
    private val viewTextView: TextView = itemView.findViewById(R.id.viewTextView)

    fun bind(item: Item) {
        nameTextView.text = item.name
        priceTextView.text = "Price: ${item.price}"
        viewTextView.text = "Views: ${item.view}"
        val url = "http://192.168.1.5/api/images/${item.img}"
        Picasso.get().load(url).fit().centerInside().into(imageView)
        clickOnItem(item)
    }
    private fun clickOnItem(item: Item) {
        itemView.setOnClickListener {
            val intent = Intent(context, detail_product::class.java)
            intent.putExtra("item", item.id)
            intent.putExtra("view", item.view)
            intent.putExtra("img", item.img)
            intent.putExtra("name", item.name)
            intent.putExtra("price", item.price)
            intent.putExtra("des", item.des)
            intent.putExtra("size", item.size)
            intent.putExtra("color", item.color)
            intent.putExtra("status", item.status)

            context.startActivity(intent)

        }
    }
}
