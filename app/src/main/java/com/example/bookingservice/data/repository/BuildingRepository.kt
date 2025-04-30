package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.BuildingApi
import com.example.bookingservice.data.model.Building
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BuildingRepository(
    private val buildingApi: BuildingApi
) {
    suspend fun getAllBuildings(): Result<List<Building>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = buildingApi.getAllBuildings()
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load buildings"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}