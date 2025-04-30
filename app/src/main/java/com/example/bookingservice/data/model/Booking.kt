package com.example.bookingservice.data.model

data class Booking(
    val id: String,
    val userId: String,
    val roomId: String,
    val startTime: String, // Формат ISO 8601, например "2023-10-25T10:00:00Z"
    val endTime: String,
    val status: String, // Например: "confirmed", "pending", "cancelled"
)