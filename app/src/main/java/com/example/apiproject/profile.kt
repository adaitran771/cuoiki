package com.example.apiproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apiproject.API.apiServiceUser
import com.example.apiproject.API.userResponse


import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class profile : AppCompatActivity() {
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var btnHome: Button
    private lateinit var phoneTextView: TextView
    private lateinit var addressTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameTextView = findViewById(R.id.profile_name)
        emailTextView = findViewById(R.id.profile_email)
        btnHome = findViewById(R.id.home_button)

        phoneTextView = findViewById(R.id.profile_phone)
        addressTextView = findViewById(R.id.profile_address)

        btnHome.setOnClickListener {
            val intent = Intent(this@profile, MainActivity::class.java)
            startActivity(intent)
        }

        // Get user profile data
        getUserProfile()
    }

    private fun getUserProfile() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "")

        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Username không tồn tại", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.100.7/") // Thay đổi baseUrl nếu cần
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(apiServiceUser::class.java)
        val call = api.getUserByUsername(username)

        call.enqueue(object : Callback<userResponse> {
            override fun onResponse(call: Call<userResponse>, response: Response<userResponse>) {
                if(response.isSuccessful) {
                    val responseData = response.body()?.response
                    if (responseData != null && responseData.data != null && responseData.data.isNotEmpty()) {
                        val userProfile = responseData.data[0]
                        Log.d("UserProfile", "Name: ${userProfile.name}, Email: ${userProfile.email}, Phone: ${userProfile.phone}, Address: ${userProfile.address}")
                        // Hiển thị thông tin người dùng
                        nameTextView.text = "${userProfile.name}"
                        emailTextView.text = "Email: ${userProfile.email}"
                        phoneTextView.text = "Phone: ${userProfile.phone}"
                        addressTextView.text = "Address: ${userProfile.address}"
                    } else {
                        Log.d("UserProfile", "Empty user profile")
                        Toast.makeText(this@profile, "Empty user profile", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@profile, "Failed to get server response", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<userResponse>, t: Throwable) {
                Toast.makeText(this@profile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
