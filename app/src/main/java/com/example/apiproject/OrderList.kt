package com.example.apiproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.viewpager2.widget.ViewPager2
import com.example.apiproject.Adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class OrderList : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        val exitHome : Button = findViewById(R.id.exitHome)
        exitHome.setOnClickListener {
            val intent = Intent(this, MainActivity
            ::class.java)
            startActivity(intent)
        }

        tabLayout = findViewById(R.id.tabLayout)
        viewPager = findViewById(R.id.viewPager)
        viewPagerAdapter = ViewPagerAdapter(this)

        viewPager.adapter = viewPagerAdapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->

            when (position) {
                0 -> tab.text = "Chờ xác nhận"
                1 -> tab.text = "Đang Giao"
                2 -> tab.text = "Đã Nhận"
                3 -> tab.text = "Đã Hủy"

            }
        }.attach()
    }
}