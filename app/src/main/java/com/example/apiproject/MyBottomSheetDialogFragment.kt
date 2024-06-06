package com.example.apiproject

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast
import com.example.apiproject.API.apiServiceOrder
import com.example.apiproject.API.serverResponse
import com.example.apiproject.order.AuthInterceptor
import com.example.apiproject.order.Order
import com.example.apiproject.product.Product
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import okhttp3.OkHttp
import okhttp3.OkHttpClient
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MyBottomSheetDialogFragment(private val product : Product)
    : BottomSheetDialogFragment() {
    private var isCheckedSize = false
    private var isCheckedColor = false
    private var IdSizeBtnCheck = -1
    private var IdColorBtnCheck = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bottom_sheet, container, false)
        val sizeLayout = view.findViewById<LinearLayout>(R.id.sizeLayout)
        val colorLayout = view.findViewById<LinearLayout>(R.id.colorLayout)
        val totalPriceTxt = view.findViewById<TextView>(R.id.productPrice)
        val quanlityTxt = view.findViewById<TextView>(R.id.text_quantity)
        //lấy đối tượng img hiện ảnh sản phẩm
        val productImg = view.findViewById<ImageView>(R.id.imgMini)
        val productName = view.findViewById<TextView>(R.id.productName)
        val productPrice = view.findViewById<TextView>(R.id.productPrice)
        val btnMinus = view.findViewById<Button>(R.id.button_decrease)
        val btnPlus = view.findViewById<Button>(R.id.button_increase)
        val btnOrder = view.findViewById<Button>(R.id.btnOrder)
        //

       //
        //chèn ảnh vào
        val itemImg = product.img// Lấy URL hình ảnh từ Intent
        // Tải và hiển thị hình ảnh sản phẩm
        itemImg?.let {

            Picasso.get().load("http://192.168.1.3/api/images/$it").fit().centerInside().into(productImg)
        }
        productName.text = product.name
        productPrice.text = product.price.toString()


        //tạo dòng size
        val textSize = TextView(context)
        textSize.text = "SIZE"
        //tạo dòng color
        val textColor = TextView(context)
        textColor.text = "COLOR"

        // Tạo và thêm RadioButtons cho kích thước
        val sizeRadioGroup = RadioGroup(context)
        sizeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            IdSizeBtnCheck = checkedId
        }
        sizeRadioGroup.orientation = RadioGroup.VERTICAL

        if (product.size != null) {
            for (size in product.size) {
                val radioButton = RadioButton(context)
                radioButton.text = "Size: $size"
                radioButton.id = View.generateViewId()
                sizeRadioGroup.addView(radioButton)
            }
        }
        sizeLayout.addView(textSize)
        sizeLayout.addView(sizeRadioGroup)



        // Tạo và thêm RadioButtons cho màu sắc
        val colorRadioGroup = RadioGroup(context)
        colorRadioGroup.orientation = RadioGroup.VERTICAL
        colorRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            IdColorBtnCheck = checkedId
        }

        if (product.color != null) {
            for (color in product.color) {
                val radioButton = RadioButton(context)
                radioButton.text = color
                radioButton.id = View.generateViewId()
                colorRadioGroup.addView(radioButton)
            }
        }
        colorLayout.addView(textColor)
        colorLayout.addView(colorRadioGroup)



        btnPlus.setOnClickListener {
            plus(quanlityTxt,totalPriceTxt,product.price )
        }
        btnMinus.setOnClickListener {
            minus(quanlityTxt,totalPriceTxt,product.price)
        }
        //xử lí đặt hàng ở đây

        btnOrder.setOnClickListener {
            if(!isSizeSelected(IdSizeBtnCheck)) {
                Toast.makeText(context,"Vui lòng chọn Size !",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(!isColorSelected(IdColorBtnCheck)) {
                Toast.makeText(context,"Vui lòng chọn Màu !",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val sizeSelected = getSelectedSize(IdSizeBtnCheck)
            var colorSelected = getSelectedColor(IdColorBtnCheck)
            val order = Order(
                id = "",
                productName = product.name,
                size = sizeSelected,
                color = colorSelected,
                price = productPrice.text.toString(),
                quantity = quanlityTxt.text.toString(),
                idProduct = product.id.toString(),
                status = "",
                id_account = "",
                img = ""


            )
            val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
            // Lấy giá trị JWT_TOKEN
            val jwtToken = sharedPreferences.getString("JWT_TOKEN", null)

            if(jwtToken != null) {
                val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(AuthInterceptor(jwtToken))
                    .build()
                Log.d("JWT","${jwtToken}")
                val api = Retrofit.Builder().
                baseUrl("http://192.168.1.3/")
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(apiServiceOrder::class.java)
                val call = api.createOrder(order)
                call.enqueue(object : Callback<serverResponse> {
                    override fun onResponse(
                        call: Call<serverResponse>,
                        response: Response<serverResponse>
                    ) {
                        if(response.isSuccessful) {
                            Log.d("message","${response.body().toString()}")
                            Toast.makeText(context, "OK",Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<serverResponse>, t: Throwable) {
                        Toast.makeText(context, "Failed !",Toast.LENGTH_LONG).show()
                    }

                })
            }




        }

        return view


    }

    private fun plus(quanlityText: TextView, totalPrice: TextView,pricePerOne: Float) {
        val current = quanlityText.text.toString().toInt()
        quanlityText.text = (current + 1).toString()
        totalPrice.text = (pricePerOne*(current + 1)).toString()
    }
    private fun minus(quanlityText: TextView, totalPrice: TextView,pricePerOne: Float) {

        val current = quanlityText.text.toString().toInt()
        if(current == 1)
            return

        quanlityText.text = (current - 1).toString()
        totalPrice.text = (pricePerOne*(current - 1)).toString()
    }
    //hàm lấy size
    private fun getSelectedSize(selectedRadioButtonId : Int): String? {


        val selectedRadioButton = selectedRadioButtonId?.let { view?.findViewById<RadioButton>(it) }
        return selectedRadioButton?.text?.toString()
    }
    //hàm kiểm tra đã check size chưa
    private fun isSizeSelected(checkedRadioButtonId : Int ): Boolean {

        return checkedRadioButtonId != -1
    }
    //hàm lấy color
    private fun getSelectedColor(selectedRadioButtonId : Int): String? {

        val selectedRadioButton = selectedRadioButtonId?.let { view?.findViewById<RadioButton>(it) }
        return selectedRadioButton?.text?.toString()
    }
    private fun isColorSelected(checkedRadioButtonId: Int): Boolean {
        return checkedRadioButtonId != -1
    }

}