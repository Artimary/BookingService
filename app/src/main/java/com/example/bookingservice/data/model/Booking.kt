package com.example.bookingservice.data.model

data class Booking(
    val id: String,
    val user_id: String,
    val room_id: String,
    val start_time: String, // Формат ISO 8601, например "2023-10-25T10:00:00Z"
    val end_time: String,
    val status: String, // Например: "confirmed", "pending", "cancelled"
)