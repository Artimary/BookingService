package com.example.bookingservice.data.model

data class LoginRequest(
    val username: String, // name or email
    val password: String
)