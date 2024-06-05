package com.example.apiproject.API

import com.example.apiproject.user.User
import com.example.apiproject.user.UserRegister
import retrofit2.Call
import retrofit2.http.*

data class serverResponse(
    val response: ResponseData
)

data class  userResponse(
    val response: ResponseUser

)

data class ResponseUser(
    val status: Int,
    val data: List<UserProfile>? // Đổi tên từ Data thành UserProfile để dễ hiểu hơn
)
data class ResponseData(
    val status: Int,
    val data: JwtData?,

    )

data class UserProfile(
    val id: String,
    val username: String,
    val password: String,
    val email: String,
    val name: String,
    val phone: String,
    val address: String,
    val role: String,
    val accessToken: String
)


data class JwtData(
    val jwt: String,
    val message: String,
    val user: User
)

interface apiServiceUser {
    @Headers("Accept: application/json")
    @POST("api/users/login")
    fun login(@Body user: User): Call<serverResponse>

    @Headers("Accept: application/json")
    @POST("api/users/register")
    fun register(@Body user: UserRegister): Call<serverResponse>

    @Headers("Accept: application/json")
    @GET("api/users")
    fun getUserByUsername(@Query("username") username: String): Call<userResponse>
}
