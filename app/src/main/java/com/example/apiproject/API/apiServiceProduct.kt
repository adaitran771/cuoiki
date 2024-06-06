package com.example.apiproject.API

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

data class ProductResponse(
    val response: ResponseDataProduct
)

data class ResponseDataProduct(
    val status: String,
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
    fun paging(@Query("page") page: Int): Call<ProductResponse>

    @Headers("Content-Type: application/json")
    @GET("api/products")
    fun search(@Query("productName") productName: String): Call<ProductResponse>

    @Headers("Content-Type: application/json")
    @GET("api/products")
    fun filterByPrice(@Query("minPrice") minPrice: Double?, @Query("maxPrice") maxPrice: Double?): Call<ProductResponse>
}
