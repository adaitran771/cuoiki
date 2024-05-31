package com.example.apiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.Item
import com.example.apiproject.API.ProductResponse
import com.example.apiproject.API.apiServiceProduct
import com.example.apiproject.API.serverResponse
import com.example.apiproject.Adapter.ItemAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var listItem: List<Item>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)



        //api url
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        val apiProduct = retrofit.create(apiServiceProduct::class.java)
        val page = 1
        val call = apiProduct.paging(page)
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if(response.isSuccessful) {
                    listItem = response.body()?.response!!.data
                    Toast.makeText(this@MainActivity,"OK", Toast.LENGTH_SHORT).show()
                    //gan vao ryclerview
                    adapter = ItemAdapter(listItem)
                    recyclerView.adapter = adapter
                    adapter.updateItems(listItem)
                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Failed", Toast.LENGTH_SHORT).show()
            }

        })



//

    }


}