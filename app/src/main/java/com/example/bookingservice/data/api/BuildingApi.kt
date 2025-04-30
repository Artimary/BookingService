package com.example.bookingservice.data.api

import com.example.bookingservice.data.model.Building
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class CreateBuildingRequest(val name: String, val address: String)

interface BuildingApi {
    @POST("buildings/")
    suspend fun createBuilding(@Body request: CreateBuildingRequest): Response<Building>

    @GET("buildings/{id}")
    suspend fun getBuilding(@Path("id") id: String): Response<Building>

    @GET("buildings/all")
    suspend fun getAllBuildings(): Response<List<Building>>
}