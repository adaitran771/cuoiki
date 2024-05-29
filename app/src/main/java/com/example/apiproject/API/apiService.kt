package com.example.apiproject.API

import android.telecom.Call
import com.example.apiproject.user.User
import retrofit2.http.Body


import retrofit2.http.Headers
import retrofit2.http.POST
data class serverResponse(
    val response: ResponseData
)

data class ResponseData(
    val status: Int,
    val data: JwtData
)

data class JwtData(
    val jwt: String
)

interface apiService {
    @Headers("Accept: application/json")

    @POST("api/auth/login")
    fun login(@Body user: User) : retrofit2.Call<serverResponse>
}