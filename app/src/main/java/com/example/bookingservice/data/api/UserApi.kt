package com.example.bookingservice.data.api

import com.example.bookingservice.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

data class UpdateUserRequest(
    val email: String? = null,
    val password: String? = null
)

interface UserApi {
    @PUT("users/{id}")
    suspend fun updateUser(
        @Path("id") id: String,
        @Body request: UpdateUserRequest
    ): Response<User>
}
