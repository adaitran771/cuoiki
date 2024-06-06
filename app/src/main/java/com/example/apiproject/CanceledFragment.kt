package com.example.apiproject

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.StatusOrder
import com.example.apiproject.API.apiServiceOrder
import com.example.apiproject.Adapter.ItemOrderAdapter
import com.example.apiproject.order.AuthInterceptor
import com.example.apiproject.order.Order
import com.example.apiproject.order.getOrderByStatus
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CanceledFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemOrderAdapter
    private lateinit var listItem: MutableList<Order>
    private val CANCELEDSTATUS = 4
    private lateinit var apiOrder : apiServiceOrder

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context = requireContext()
        val view = inflater.inflate(R.layout.fragment_order, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)
        var layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager

        listItem  = mutableListOf()

        //gan vao ryclerview
        adapter = ItemOrderAdapter(listItem as MutableList<Any?>, context,CANCELEDSTATUS )
        recyclerView.adapter = adapter

        //api url
        val sharedPreferences = requireContext().getSharedPreferences("MyAppPrefs",
            Context.MODE_PRIVATE
        )
        // Lấy giá trị JWT_TOKEN
        val jwtToken = sharedPreferences.getString("JWT_TOKEN", null)
        if(jwtToken != null) {
//            val gson: Gson = GsonBuilder()
//                .setLenient()
//                .create()
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(AuthInterceptor(jwtToken))
                .build()
            val retrofit = Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://192.168.170.64/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            apiOrder = retrofit.create(apiServiceOrder::class.java)

            getOrderByStatus(apiOrder, StatusOrder(status = CANCELEDSTATUS)) {
                Log.d("data","${println(it)}")
                listItem.addAll(it)
                adapter.notifyDataSetChanged()
            }
        } else {
            Toast.makeText(context, "Ban chua dang nhap, vui long dang nhap !", Toast.LENGTH_LONG)
        }
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Kiểm tra nếu đã cuộn tới cuối danh sách
                if (!recyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                    // Đã cuộn tới cuối danh sách
                    Toast.makeText(requireContext(), "Reload", Toast.LENGTH_SHORT).show()
                    getOrderByStatus(apiOrder, StatusOrder(status = CANCELEDSTATUS)) {
                        Log.d("data","${println(it)}")
                        listItem.clear()
                        listItem.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }

                // Kiểm tra nếu đã cuộn tới đầu danh sách
                if (!recyclerView.canScrollVertically(-1)) {
                    // Đã cuộn tới đầu danh sách
                    Toast.makeText(requireContext(), "Reload", Toast.LENGTH_SHORT).show()
                    getOrderByStatus(apiOrder, StatusOrder(status = CANCELEDSTATUS)) {
                        Log.d("data","${println(it)}")
                        listItem.clear()
                        listItem.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                }
            }
        })


        return view
    }
}
