package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.RoomApi
import com.example.bookingservice.data.model.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomRepository(
    private val roomApi: RoomApi
) {
    suspend fun getRoomsByBuilding(buildingId: String): Result<List<Room>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = roomApi.getRoomsByBuilding(buildingId)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load rooms"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}