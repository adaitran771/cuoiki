package com.example.apiproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.SearchView

import androidx.recyclerview.widget.GridLayoutManager

import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.Item
import com.example.apiproject.API.ProductResponse
import com.example.apiproject.API.apiServiceProduct

import com.example.apiproject.Adapter.ItemAdapter

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var listItem: MutableList<Item>
    private lateinit var listItemSearched: MutableList<Item>
    private lateinit var searchView : SearchView
    private var isLoading = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Ánh xạ container trong layout cha
        val navigationContainer: FrameLayout = findViewById(R.id.navigation_container)

        // Inflate layout con và thêm vào container
        val inflater: LayoutInflater = LayoutInflater.from(this)
        val navigationBar: View = inflater.inflate(R.layout.navigation_bar, navigationContainer, false)

        // Thêm layout con vào container

        val NavigationHandler : NavigationHandler
        = NavigationHandler(this,navigationBar, navigationContainer)
        NavigationHandler.insertNavIntoParent()
        NavigationHandler.setClickNav()


        searchView = findViewById(R.id.searchView)
        recyclerView = findViewById(R.id.recycler_view)
        var layoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = layoutManager



        //api url
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.7/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
        val apiProduct = retrofit.create(apiServiceProduct::class.java)
        var page = 1
        listItem  = mutableListOf()
        //gan vao ryclerview
        adapter = ItemAdapter(listItem as MutableList<Any?>, this )
        recyclerView.adapter = adapter
//        adapter.updateItems(listItem)
        getDataServerPaging(apiProduct, page) {
            listItem.addAll(it)
            adapter.notifyDataSetChanged()
        }


        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0) {
                    page += 1
                    loadMoreItems(apiProduct, page)
                }
            }
        })
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Xử lý khi người dùng nhấn nút tìm kiếm hoặc gửi truy vấn
                // Ở đây bạn có thể gọi phương thức để tìm kiếm sản phẩm dựa trên query
                searchProduct(apiProduct, query ){
                    listItemSearched = it
                    adapter.setResourceAdapter(listItemSearched as MutableList<Any?>)
                    adapter.notifyDataSetChanged()


                }
                searchView.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Xử lý khi người dùng thay đổi văn bản trong ô tìm kiếm
                // Ở đây bạn có thể thực hiện việc tìm kiếm sản phẩm ngay khi người dùng nhập liệu
                return true
            }
        })








    }
    private fun loadMoreItems(api : apiServiceProduct, page: Int) {
        isLoading = true
        adapter.addLoadingFooter()

        // Simulate a delay for loading more items
        recyclerView.postDelayed({
            // Remove loading footer
            adapter.removeLoadingFooter()

            // Add new data
            var newOne = Item("0","?","?","?","?",
                "?","?","?","?","?")
            var newData : MutableList<Item> = mutableListOf(newOne)
            getDataServerPaging(api, page) {
                newData = it
                adapter.addDataToAdapter( newData)
                isLoading = false
            }




        }, 2000)
    }
    private fun getDataServerPaging (api : apiServiceProduct, page : Int, result : (MutableList<Item>)->Unit ){
        val call = api.paging(page)
        var list: MutableList<Item>
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if(response.isSuccessful) {

                    Toast.makeText(this@MainActivity,"OK", Toast.LENGTH_SHORT).show()

                    list = response.body()?.response!!.data as MutableList<Item>
                    result.invoke(list)


                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Failed", Toast.LENGTH_SHORT).show()
            }

        })



    }

    private fun searchProduct(api: apiServiceProduct, productName: String, result : (MutableList<Item>)->Unit) {
        var list : MutableList<Item>
        val call = api.search(productName)
        call.enqueue(object : Callback<ProductResponse> {
            override fun onResponse(
                call: Call<ProductResponse>,
                response: Response<ProductResponse>
            ) {
                if(response.isSuccessful) {

                    Toast.makeText(this@MainActivity,"OK", Toast.LENGTH_SHORT).show()

                    list = response.body()?.response!!.data as MutableList<Item>
                    result.invoke(list)


                }
            }

            override fun onFailure(call: Call<ProductResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity,"Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

//


}