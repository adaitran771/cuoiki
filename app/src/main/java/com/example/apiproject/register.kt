package com.example.apiproject

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.apiproject.API.serverResponse
import com.example.apiproject.API.apiServiceUser
import com.example.apiproject.user.UserRegister
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class register : AppCompatActivity() {
    private lateinit var username: TextInputEditText
    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var confirmPassword: TextInputEditText
    private lateinit var name: TextInputEditText
    private lateinit var phone: TextInputEditText
    private lateinit var address: TextInputEditText
    private lateinit var btnRegister: Button
    private lateinit var loginLink: TextView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // Initialize UI components
        username = findViewById(R.id.username)
        email = findViewById(R.id.registerEmail)
        password = findViewById(R.id.registerPassword)
        confirmPassword = findViewById(R.id.confirmPassword)
        name = findViewById(R.id.fullName)
        phone = findViewById(R.id.phone)
        address = findViewById(R.id.address)
        btnRegister = findViewById(R.id.register)
        loginLink = findViewById(R.id.loginLink)
        recyclerView = findViewById(R.id.recyclerView)

        btnRegister.setOnClickListener {
            if (validateFields()) {
                performRegister()
            }
        }

        loginLink.setOnClickListener {
            val intent = Intent(this@register, login::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        val user = UserRegister(
            username.text.toString(),
            email.text.toString(),
            password.text.toString(),
            name.text.toString(),
            phone.text.toString(),
            address.text.toString(),
            confirmPassword.text.toString()
        )

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.62/") // Change the baseUrl if needed
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(apiServiceUser::class.java)
        val call = api.register(user)
        call.enqueue(object : Callback<serverResponse> {
            override fun onResponse(call: Call<serverResponse>, response: Response<serverResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@register, "Đăng ký thành công!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@register, login::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this@register, "Đăng ký thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<serverResponse>, t: Throwable) {
                Toast.makeText(this@register, "Đăng ký thất bại: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun validateFields(): Boolean {
        // Convert TextInputEditText fields to String
        val usernameText = username.text.toString()
        val emailText = email.text.toString()
        val passwordText = password.text.toString()
        val confirmPasswordText = confirmPassword.text.toString()
        val nameText = name.text.toString()
        val phoneText = phone.text.toString()
        val addressText = address.text.toString()

        // Check if any field is empty
        if (usernameText.isEmpty() || emailText.isEmpty() || passwordText.isEmpty() ||
            confirmPasswordText.isEmpty() || nameText.isEmpty() || phoneText.isEmpty() ||
            addressText.isEmpty()
        ) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return false
        }

        if (usernameText.length < 6) {
            Toast.makeText(this, "Tên đăng nhập phải chứa ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate email format
        if (!Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            Toast.makeText(this, "Vui lòng nhập email hợp lệ", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check password strength (example: at least 6 characters)
        if (passwordText.length < 6) {
            Toast.makeText(this, "Mật khẩu phải chứa ít nhất 6 ký tự", Toast.LENGTH_SHORT).show()
            return false
        }

        // Confirm password match
        if (passwordText != confirmPasswordText) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validate phone number format (example: 10 digits)
        val phoneNumberPattern = "\\d{10}".toRegex()
        if (!phoneNumberPattern.matches(phoneText)) {
            Toast.makeText(this, "Vui lòng nhập số điện thoại hợp lệ (10 chữ số)", Toast.LENGTH_SHORT).show()
            return false
        }

        // You can add more specific address validation if needed

        return true
    }


}
