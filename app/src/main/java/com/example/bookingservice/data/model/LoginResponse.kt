package com.example.bookingservice.data.model

data class LoginResponse(
    val token: String, // Пример: токен авторизации
    val success: Boolean
)