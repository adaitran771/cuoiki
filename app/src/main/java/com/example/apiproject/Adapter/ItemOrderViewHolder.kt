package com.example.apiproject.Adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.apiServiceOrder

import com.example.apiproject.R
import com.example.apiproject.order.AuthInterceptor
import com.example.apiproject.order.Order
import com.example.apiproject.order.UpdateStatusOrder

import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ItemOrderViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {
    private val imageView: ImageView = itemView.findViewById(R.id.imgOrder)
    private val nameTextView: TextView = itemView.findViewById(R.id.orderName)
    private val priceTextView: TextView = itemView.findViewById(R.id.totalPrice)
    private val quantityTextView: TextView = itemView.findViewById(R.id.quantity)
    private val colorTextView: TextView = itemView.findViewById(R.id.color)
    private val sizeTextView: TextView = itemView.findViewById(R.id.size)
    private val btnStatus: TextView = itemView.findViewById(R.id.btnStatus)
    fun bind(item: Order, status: Int) {
        nameTextView.text = item.productName
        priceTextView.text = item.price
        quantityTextView.text = item.quantity
        sizeTextView.text = item.size
        colorTextView.text = item.color
        val url = "http://192.168.1.4/api/images/${item.img}"
        Picasso.get().load(url).fit().centerInside().into(imageView)
        clickOnItem(item)
        if(status != 1 ) {
            btnStatus.isClickable = false
            btnStatus.visibility = INVISIBLE
            btnStatus.setBackgroundColor(Color.parseColor("#654242"))
        } else {
            btnStatus.setOnClickListener {
                val sharedPreferences = context.getSharedPreferences("MyAppPrefs",
                    Context.MODE_PRIVATE
                )
                // Lấy giá trị JWT_TOKEN
                val jwtToken = sharedPreferences.getString("JWT_TOKEN", null)
                if(jwtToken != null) {
//            val gson: Gson = GsonBuilder()
//                .setLenient()
//                .create()
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    val okHttpClient = OkHttpClient.Builder()
                        .addInterceptor(logging)
                        .addInterceptor(AuthInterceptor(jwtToken))
                        .build()
                    val retrofit = Retrofit.Builder()
                        .client(okHttpClient)
                        .baseUrl("http://192.168.1.3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                    val apiOrder = retrofit.create(apiServiceOrder::class.java)
                    UpdateStatusOrder(item, apiOrder) {
                        if(it == 200) {
                            Toast.makeText(context, "Hủy Đơn Thành Công",Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, "Hủy Đơn Thất bại",Toast.LENGTH_LONG).show()
                        }
                    }
                }

        }
        //Log.d("VariableType", item.price.javaClass.toString())

    }
    }
    private fun clickOnItem(item: Order) {
//        itemView.setOnClickListener {
//            val intent = Intent(context, detail_product::class.java)
//            intent.putExtra("item", item.id)
//            intent.putExtra("view", item.view)
//            intent.putExtra("img", item.img)
//            intent.putExtra("name", item.name)
//            intent.putExtra("price", item.price.toFloat())
//            intent.putExtra("des", item.des)
//            intent.putExtra("size", item.size)
//            intent.putExtra("color", item.color)
//            intent.putExtra("status", item.status)
//
//            context.startActivity(intent)

 //       }
    }
}