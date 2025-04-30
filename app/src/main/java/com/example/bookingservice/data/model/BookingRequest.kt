package com.example.bookingservice.data.model

data class BookingRequest(
    val roomId: String,
    val startTime: String,
    val endTime: String
)