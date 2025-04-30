package com.example.bookingservice.data.model

data class Room(
    val id: String,
    val buildingId: String,
    val name: String,
    val type: String, // Например: "lecture", "meeting", "computer"
    val capacity: Int,
    val status: String // Например: "free", "busy", "unavailable"
)