package com.example.apiproject.product

import java.io.Serializable

class Product (
    val id: Int?,
    val img: String?,
    val name: String?,
    val des: String?,
    var view: Int?,
    var status: String?,
    val size: List<Int>?,
    val color: List<String>?,
    var price: Float
) : Serializable {
    // Các hàm khác bạn muốn thêm vào đây
}