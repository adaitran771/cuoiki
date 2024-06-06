package com.example.apiproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
    private lateinit var phoneTextView: TextView
    private lateinit var addressTextView: TextView

    private lateinit var btnHome: Button
    private lateinit var btnEditProfile: Button
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        nameTextView = findViewById(R.id.profile_name)
        emailTextView = findViewById(R.id.profile_email)
        phoneTextView = findViewById(R.id.profile_phone)
        addressTextView = findViewById(R.id.profile_address)

        btnHome = findViewById(R.id.home_button)
        btnEditProfile = findViewById(R.id.edit_info)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)

        btnHome.setOnClickListener {
            val intent = Intent(this@profile, MainActivity::class.java)
            startActivity(intent)
        }

        btnEditProfile.setOnClickListener {
            val intent = Intent(this@profile, EditProfile::class.java)
            startActivity(intent)
        }

        // Setup SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener {
            // Refresh user profile data when swiped down
            getUserProfile()
        }

        // Get user profile data
        getUserProfile()
    }

    private fun getUserProfile() {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "")

        if (username.isNullOrEmpty()) {
            Toast.makeText(this, "Username không tồn tại", Toast.LENGTH_SHORT).show()
            swipeRefreshLayout.isRefreshing = false // Stop refreshing
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.62/") // Update baseUrl if needed
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

                        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("ID", userProfile.id)
                        editor.putString("USERNAME", userProfile.username)
                        editor.putString("NAME", userProfile.name)
                        editor.putString("EMAIL", userProfile.email)
                        editor.putString("PHONE", userProfile.phone)
                        editor.putString("ADDRESS", userProfile.address)
                        editor.apply()

                        Log.d("UserProfile", "Name: ${userProfile.name}, Email: ${userProfile.email}, Phone: ${userProfile.phone}, Address: ${userProfile.address}")
                        // Display user profile info
                        nameTextView.text = userProfile.name
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
                swipeRefreshLayout.isRefreshing = false // Stop refreshing
            }

            override fun onFailure(call: Call<userResponse>, t: Throwable) {
                Toast.makeText(this@profile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                swipeRefreshLayout.isRefreshing = false // Stop refreshing
            }
        })
    }
}
