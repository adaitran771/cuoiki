package com.example.apiproject.API
import com.example.apiproject.user.User
import retrofit2.http.Body
import retrofit2.Call
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
    val jwt: String,
    val message: String,
)

interface apiService {
    @Headers("Accept: application/json")

    @POST("api/users/login")
    fun login(@Body user: User) : Call<serverResponse>
}