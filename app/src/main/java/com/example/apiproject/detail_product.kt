package com.example.apiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class detail_product : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_product)
        // Ánh xạ các view
        val productImg: ImageView = findViewById(R.id.productImg)
       // val priceLable: TextView = findViewById(R.id.priceLable)
        val price: TextView = findViewById(R.id.price)
       // val productLable: TextView = findViewById(R.id.productLable)
        val productName: TextView = findViewById(R.id.productName)
       // val desLable: TextView = findViewById(R.id.desLable)
        val des: TextView = findViewById(R.id.des)
        val commentLable: TextView = findViewById(R.id.commentLable)
        val commentRecyclerView: RecyclerView = findViewById(R.id.commentRecyclerView)
        val color : TextView  = findViewById(R.id.productColorLabel)
        val status : TextView = findViewById(R.id.productStatus)
        val size : TextView = findViewById(R.id.productSize)







        // Lấy dữ liệu từ Intent
        val itemId = intent.getIntExtra("item", -1)
        val itemView = intent.getIntExtra("view", 0)
        val itemImg = intent.getStringExtra("img")
        val itemName = intent.getStringExtra("name")
        val itemPrice = intent.getDoubleExtra("price", 0.0)
        val itemDes = intent.getStringExtra("des")
        val itemSize = intent.getStringExtra("size")
        val itemColor = intent.getStringExtra("color")
        val itemStatus = intent.getStringExtra("status")

        // Gán dữ liệu cho các view
        productName.text = itemName
        price.text = "Price: $itemPrice"
        des.text = itemDes
        color.text = itemColor
        size.text = itemSize
        status.text = itemStatus


        // Tải lại ảnh
        itemImg?.let {
            Picasso.get().load("http://192.168.1.4/api/images/$it").fit().centerInside().into(productImg)
        }
        val navigationContainer: FrameLayout = findViewById(R.id.navigation_container)

        // Inflate layout con và thêm vào container
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val navigationBar: View = inflater.inflate(R.layout.navigation_bar, navigationContainer, false)

        val NavigationHandler : NavigationHandler = NavigationHandler(this,navigationBar, navigationContainer)
        NavigationHandler.insertNavIntoParent()
        NavigationHandler.setClickNav()
    }
}