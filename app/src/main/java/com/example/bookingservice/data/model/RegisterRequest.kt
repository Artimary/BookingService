package com.example.bookingservice.data.model

data class RegisterRequest(
    val login: String,
    val email: String,
    val password: String
)