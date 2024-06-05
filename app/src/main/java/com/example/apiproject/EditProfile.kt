package com.example.apiproject

import android.content.Context
import android.content.Intent

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.apiproject.API.apiServiceUser
import com.example.apiproject.API.serverResponse
import com.example.apiproject.user.UserEdit
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class EditProfile : AppCompatActivity() {

    private lateinit var usernameEditText: TextInputEditText
    private lateinit var nameEditText: TextInputEditText
    private lateinit var emailEditText: TextInputEditText
    private lateinit var phoneEditText: TextInputEditText
    private lateinit var addressEditText: TextInputEditText
    private lateinit var saveButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        usernameEditText = findViewById(R.id.username)
        nameEditText = findViewById(R.id.fullName)
        emailEditText = findViewById(R.id.email)
        phoneEditText = findViewById(R.id.phone)
        addressEditText = findViewById(R.id.address)
        saveButton = findViewById(R.id.save_button)
        cancelButton = findViewById(R.id.cancel_button)

        // Không cho phép chỉnh sửa trường nhập username
        usernameEditText.apply {
            isFocusable = false
            isClickable = false
        }

        // Lấy thông tin người dùng từ SharedPreferences
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "")
        val name = sharedPreferences.getString("NAME", "")
        val email = sharedPreferences.getString("EMAIL", "")
        val phone = sharedPreferences.getString("PHONE", "")
        val address = sharedPreferences.getString("ADDRESS", "")


        // Điền thông tin người dùng vào các trường nhập liệu
        usernameEditText.setText(username)
        nameEditText.setText(name)
        emailEditText.setText(email)
        phoneEditText.setText(phone)
        addressEditText.setText(address)

        saveButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedEmail = emailEditText.text.toString()
            val updatedPhone = phoneEditText.text.toString()
            val updatedAddress = addressEditText.text.toString()

            // Cập nhật thông tin người dùng trên server và lưu vào SharedPreferences
            updateUserProfile(username, updatedName, updatedEmail, updatedPhone, updatedAddress)
        }

        cancelButton.setOnClickListener {
            // Quay lại màn hình trước đó
            intent = Intent(this@EditProfile,profile::class.java)
            startActivity(intent)
        }
    }

    private fun updateUserProfile(username: String?, name: String, email: String, phone: String, address: String) {
        if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.1.62/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(apiServiceUser::class.java)

        val userEdit = UserEdit(
            username = username ?: "",
            email = email,
            name = name,
            phone = phone,
            address = address
        )

        val call = api.update(userEdit)

        call.enqueue(object : Callback<serverResponse> {
            override fun onResponse(call: Call<serverResponse>, response: Response<serverResponse>) {
                if (response.isSuccessful) {
                    // Cập nhật thông tin người dùng trong SharedPreferences
                    val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("NAME", name)
                    editor.putString("EMAIL", email)
                    editor.putString("PHONE", phone)
                    editor.putString("ADDRESS", address)
                    editor.apply()

                    // Hiển thị thông báo cập nhật thành công
                    Toast.makeText(this@EditProfile, "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show()
                } else {
                    // Hiển thị thông báo cập nhật thất bại
                    Toast.makeText(this@EditProfile, "Cập nhật thông tin thất bại", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<serverResponse>, t: Throwable) {
                // Hiển thị thông báo lỗi nếu không thể cập nhật thông tin
                Toast.makeText(this@EditProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
