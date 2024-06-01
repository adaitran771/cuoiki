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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.et_email)
        password = findViewById(R.id.et_password)
        btnLogin = findViewById(R.id.button)
        btnLogin.setOnClickListener {
            Toast.makeText(this@login, "click", Toast.LENGTH_SHORT).show()
            //check internet
            if (isNetworkAvailable(this)) {
                Toast.makeText(this, "Having internet connection", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            }



            val user = User(username.text.toString(), password.text.toString())

            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
            val Api = retrofit.create(apiServiceUser::class.java)
            val call = Api.login(User(username.text.toString(), password.text.toString()));
            call.enqueue(object : Callback<serverResponse> {
                override fun onResponse(call: Call<serverResponse>, response: Response<serverResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@login, "respone: ${response.body()}", Toast.LENGTH_LONG).show()
                        val jwt = response.body()?.response?.data?.jwt
                        // Lưu token và điều hướng người dùng
                        val sharedPreferences = getSharedPreferences("MyAppPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("JWT_TOKEN", jwt)
                        editor.apply()
                        if(jwt != null) {
                            Toast.makeText(
                                this@login,
                                "Đăng nhập thành công: $jwt",
                                Toast.LENGTH_SHORT
                            ).show()

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
                    Toast.makeText(this@login, "ko đc r", Toast.LENGTH_LONG)
                }
            })






        }



    }
    private fun validateField(username: TextView, password: TextView)  : Boolean {
        if(username.text == null ) {
            Toast.makeText(this, "username empty !", Toast.LENGTH_SHORT)
            return false
        }
        if(password.text == null ) {
            Toast.makeText(this, "username empty !", Toast.LENGTH_SHORT)
            return false
        }
        return true

   }
}