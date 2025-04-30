package com.example.bookingservice.data.api

import com.example.bookingservice.data.model.Room
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class CreateRoomRequest(
    val buildingId: String,
    val name: String,
    val capacity: Int,
    val type: String,
    val status: String? = null
)

interface RoomApi {
    @POST("rooms/")
    suspend fun createRoom(@Body request: CreateRoomRequest): Response<Room>

    @GET("rooms/{id}")
    suspend fun getRoom(@Path("id") id: String): Response<Room>

    @GET("rooms/building/{buildingId}")
    suspend fun getRoomsByBuilding(@Path("buildingId") buildingId: String): Response<List<Room>>

    @GET("rooms/all")
    suspend fun getAllRooms(): Response<List<Room>>
}