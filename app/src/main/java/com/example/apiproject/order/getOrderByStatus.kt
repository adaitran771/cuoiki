package com.example.apiproject.order

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import com.example.apiproject.API.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun getOrderByStatus (api : apiServiceOrder, status : StatusOrder, result : (MutableList<Order>)->Unit ){
    val call = api.getOrderByStatus(status)
    var list: MutableList<Order>
    call.enqueue(object : Callback<OrderResponse> {
        override fun onResponse(
            call: Call<OrderResponse>,
            response: Response<OrderResponse>
        ) {

            if(response.isSuccessful) {
                Log.d("SUCCESS","${println(response.body()?.response!!.data)}")
                val Orders : List<Order> = response.body()?.response!!.data.orders
                if(Orders != null)
                    list = Orders as MutableList<Order>
                else
                    list = mutableListOf<Order>()
                result.invoke(list)


            }else {
                Log.d("FAILED","FAILED RESPONSE")
            }
        }

        override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
            Log.d("FAILED","${t.message} ")
        }


    })



}