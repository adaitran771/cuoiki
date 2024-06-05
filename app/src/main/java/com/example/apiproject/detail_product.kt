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
        val price: TextView = findViewById(R.id.price)
        val productName: TextView = findViewById(R.id.productName)
        val des: TextView = findViewById(R.id.des)
        val commentLable: TextView = findViewById(R.id.commentLable)
        val commentRecyclerView: RecyclerView = findViewById(R.id.commentRecyclerView)
        val color: TextView = findViewById(R.id.productColorLabel)
        val status: TextView = findViewById(R.id.productStatus)
        val size: TextView = findViewById(R.id.productSize)

        // Lấy dữ liệu từ Intent
        val itemId = intent.getIntExtra("item", -1) // Lấy ID sản phẩm từ Intent
        val itemView = intent.getIntExtra("view", 0) // Lấy số lượt xem từ Intent
        val itemImg = intent.getStringExtra("img") // Lấy URL hình ảnh từ Intent
        val itemName = intent.getStringExtra("name") // Lấy tên sản phẩm từ Intent
        val itemPrice = intent.getDoubleExtra("price", 0.0) // Lấy giá sản phẩm từ Intent
        val itemDes = intent.getStringExtra("des") // Lấy mô tả sản phẩm từ Intent
        val itemSize = intent.getStringExtra("size") // Lấy kích thước sản phẩm từ Intent
        val itemColor = intent.getStringExtra("color") // Lấy màu sắc sản phẩm từ Intent
        val itemStatus = intent.getStringExtra("status") // Lấy trạng thái sản phẩm từ Intent

        // Gán dữ liệu cho các view
        productName.text = itemName // Hiển thị tên sản phẩm
        price.text = "Price: $itemPrice" // Hiển thị giá sản phẩm
        des.text = itemDes // Hiển thị mô tả sản phẩm
        color.text = itemColor // Hiển thị màu sắc sản phẩm
        size.text = itemSize // Hiển thị kích thước sản phẩm
        status.text = itemStatus // Hiển thị trạng thái sản phẩm

        // Tải và hiển thị hình ảnh sản phẩm
        itemImg?.let {
            Picasso.get().load("http://192.168.100.7/api/images/$it").fit().centerInside().into(productImg)
        }

        // Ánh xạ container trong layout cha
        val navigationContainer: FrameLayout = findViewById(R.id.navigation_container)

        // Inflate layout con và thêm vào container
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val navigationBar: View = inflater.inflate(R.layout.navigation_bar, navigationContainer, false)

        // Tạo đối tượng NavigationHandler và thiết lập navigation
        val NavigationHandler: NavigationHandler = NavigationHandler(this, navigationBar, navigationContainer)
        NavigationHandler.insertNavIntoParent() // Chèn navigation vào layout cha
        NavigationHandler.setClickNav() // Thiết lập sự kiện click cho navigation
    }
}
