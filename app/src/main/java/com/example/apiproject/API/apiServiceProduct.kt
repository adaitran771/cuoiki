package com.example.apiproject.API

import com.example.apiproject.user.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

data class ProductResponse(
    val response: ResponseDataProduct
)

data class ResponseDataProduct(
    val status: Int,
    val nextPage: Int?,
    val prevPage: Int?,
    val totalCount: Int,
    val data: List<Item>
)

data class Item(
    val id: String,
    val name: String,
    val img: String,
    val des: String,
    val view: String,
    val status: String,
    val idCategory: String,
    val size: String,
    val color: String,
    val price: String
)

interface apiServiceProduct {
    @Headers("Content-Type: application/json")

    @GET("api/products")
    fun paging(@Query("page") page: Int) : Call<ProductResponse>
}