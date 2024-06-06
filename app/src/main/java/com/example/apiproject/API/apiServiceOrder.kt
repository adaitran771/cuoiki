package com.example.apiproject.API

import com.example.apiproject.order.Order
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST


data class OrderResponse(
    val response: ResponseDataOrder
)

data class ResponseDataOrder(
    val status: Int,
    val data: JwtData
)

data class StatusOrder(val status: Int)
interface apiServiceOrder {
    @Headers("Content-Type: application/json")
    @POST("api/order")
    fun createOrder(@Body order: Order) : Call<serverResponse>
    @Headers("Content-Type: application/json")
    @POST("api/order/user")
    fun getOrderByStatus(@Body status: StatusOrder) : Call<OrderResponse>
    @Headers("Content-Type: application/json")
    @POST("api/order/user/update")
    fun cancelOrder(@Body order: Order) : Call<OrderResponse>
}