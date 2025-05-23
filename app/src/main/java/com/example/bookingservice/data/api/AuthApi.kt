package com.example.bookingservice.data.api

import com.example.bookingservice.data.model.User
import retrofit2.http.*
import retrofit2.Response

data class LoginRequest(val email: String, val password: String)
data class SignupRequest(val username: String, val email: String, val password: String)
data class AuthResponse(val user: User, val access_token: String)

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<AuthResponse>

    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<AuthResponse>

    @GET("auth/logout")
    suspend fun logout(): Response<Unit>
}

