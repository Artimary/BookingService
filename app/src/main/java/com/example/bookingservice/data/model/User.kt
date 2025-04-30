package com.example.bookingservice.data.model

data class User(
    val id: String,
    val username: String?,
    val email: String?,
    val role: String?, // "user", "admin"
)