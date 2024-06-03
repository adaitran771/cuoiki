package com.example.apiproject.user

data class UserRegister(
    var username: String,
    var email: String,
    var password: String,
    var name: String,
    var phone: String,
    var address: String,
    var confirmPassword: String
)
