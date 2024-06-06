package com.example.apiproject

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity

class NavigationHandler(private val context : Context,
                        private val childLayout: View,
                        private val parentLayout: FrameLayout) {


    fun insertNavIntoParent() {
        parentLayout.addView(childLayout)

    }
    fun setClickNav() {
        val homeNav = childLayout.findViewById<ImageView>(R.id.nav_home)
        homeNav.setOnClickListener {
            val intent : Intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
        val mallNav = childLayout.findViewById<ImageView>(R.id.nav_mall)
        mallNav.setOnClickListener {
            val intent : Intent = Intent(context,OrderList::class.java)
            context.startActivity(intent)
        }
        val profileNav = childLayout.findViewById<ImageView>(R.id.nav_profile)
        profileNav.setOnClickListener {
            val intent : Intent = Intent(context, profile::class.java)
            context.startActivity(intent)
        }
    }
}