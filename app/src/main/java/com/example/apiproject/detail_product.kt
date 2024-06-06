package com.example.apiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.product.Product
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
        val btnBuy : AppCompatButton = findViewById(R.id.btnBuy)

        // Lấy dữ liệu từ Intent
        val itemId = intent.getStringExtra("item") // Lấy ID sản phẩm từ Intent
        val itemView = intent.getIntExtra("view", 0) // Lấy số lượt xem từ Intent
        val itemImg = intent.getStringExtra("img") // Lấy URL hình ảnh từ Intent
        val itemName = intent.getStringExtra("name") // Lấy tên sản phẩm từ Intent
        val itemPrice = intent.getFloatExtra("price", 0f) // Lấy giá sản phẩm từ Intent
        val itemDes = intent.getStringExtra("des") // Lấy mô tả sản phẩm từ Intent
        val itemSize = intent.getStringExtra("size") // Lấy kích thước sản phẩm từ Intent
        val itemColor = intent.getStringExtra("color") // Lấy màu sắc sản phẩm từ Intent
        val itemStatus = intent.getStringExtra("status") // Lấy trạng thái sản phẩm từ Intent
        //xử lí chuỗi size ra mảng size
        val listSize = itemSize?.let { parseSizes(it) }
        // xử lí chuỗi color ra mảng color
        val listColor = itemColor?.let { parseColors(it) }

        // khởi tạo product
        val product = Product(
            id = itemId?.toInt(),
            img = itemImg,
            name = itemName,
            des = itemDes,
            view = itemView,
            status = itemStatus,

            size = listSize,
            color = listColor,
            price = itemPrice
        )
        // Gán dữ liệu cho các view
        productName.text = itemName // Hiển thị tên sản phẩm
        price.text = itemPrice.toString() // Hiển thị giá sản phẩm
        des.text = itemDes // Hiển thị mô tả sản phẩm
        color.text = itemColor // Hiển thị màu sắc sản phẩm
        size.text = itemSize // Hiển thị kích thước sản phẩm
        status.text = itemStatus // Hiển thị trạng thái sản phẩm

        // Tải và hiển thị hình ảnh sản phẩm
        itemImg?.let {


            Picasso.get().load("http://192.168.1.4/api/images/$it").fit().centerInside().into(productImg)

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



        // gắn sự kiện cho nút mua hàng
        btnBuy.setOnClickListener {
            val bottomSheetFragment = MyBottomSheetDialogFragment(product)
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }
    private fun parseSizes(sizeString: String): List<Int> {
        return sizeString.split(",")  // Chia chuỗi theo dấu phẩy
            .map { it.trim() }        // Loại bỏ khoảng trắng thừa xung quanh mỗi phần tử
            .filterNot { it.isEmpty() } // Loại bỏ các phần tử trống
            .mapNotNull { it.toIntOrNull() } // Chuyển đổi sang số nguyên và loại bỏ các giá trị null
    }
    private fun parseColors(colorString: String): List<String> {
        return colorString.split(",")  // Chia chuỗi theo dấu phẩy
            .map { it.trim() }         // Loại bỏ khoảng trắng thừa xung quanh mỗi phần tử
            .filterNot { it.isEmpty() } // Loại bỏ các phần tử trống
    }
}
