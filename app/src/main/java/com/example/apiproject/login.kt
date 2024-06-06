package com.example.apiproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.apiproject.API.serverResponse
import com.example.apiproject.API.apiServiceUser
import com.example.apiproject.user.User
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import com.example.apiproject.API.Utility.isNetworkAvailable
class login : AppCompatActivity() {
    lateinit var username : TextInputEditText
    lateinit var password : TextInputEditText
    lateinit var btnLogin : Button
    lateinit var createAccount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.username)
        password = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.button)
        createAccount = findViewById(R.id.createAccount)

        btnLogin.setOnClickListener {
            if (validateField(username, password)) {
                performLogin()
            }
        }

        createAccount.setOnClickListener {
            val intent = Intent(this@login, register::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin() {
        val user = User(username.text.toString(), password.text.toString())

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.3/") // Thay đổi baseUrl nếu cần
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val Api = retrofit.create(apiServiceUser::class.java)
        val call = Api.login(user)
        call.enqueue(object : Callback<serverResponse> {
            override fun onResponse(call: Call<serverResponse>, response: Response<serverResponse>) {
                if (response.isSuccessful) {
                    val jwt = response.body()?.response?.data?.jwt
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("JWT_TOKEN", jwt)
                    editor.apply()
                    if (jwt != null) {
                        Toast.makeText(this@login, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@login, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@login, "Đăng nhập thất bại: Không tìm thấy JWT", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@login, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<serverResponse>, t: Throwable) {
                Toast.makeText(this@login, "Đăng nhập thất bại ${t.message}", Toast.LENGTH_SHORT).show()
            }


        })




    }

    private fun validateField(username: TextView, password: TextView): Boolean {
        if (username.text.isNullOrEmpty() || password.text.isNullOrEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin đăng nhập", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}