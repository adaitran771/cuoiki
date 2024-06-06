package com.example.apiproject.order

import android.util.Log
import com.example.apiproject.API.OrderResponse
import com.example.apiproject.API.StatusOrder
import com.example.apiproject.API.apiServiceOrder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
fun UpdateStatusOrder(order: Order, api : apiServiceOrder, isSuccess :( Int )-> Unit) {
    val call = api.cancelOrder(order)

    call.enqueue(object : Callback<OrderResponse> {
        override fun onResponse(
            call: Call<OrderResponse>,
            response: Response<OrderResponse>
        ) {

            if(response.isSuccessful) {
                var issucess = 0
                issucess = response.body()?.response?.status!!
                isSuccess.invoke(issucess)


            }else {
                Log.d("FAILED","FAILED RESPONSE")
            }
        }

        override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
            Log.d("FAILED","${t.message} ")
        }


    })
}


