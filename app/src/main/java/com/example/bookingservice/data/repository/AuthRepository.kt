package com.example.bookingservice.data.repository

import android.content.Context
import com.example.bookingservice.data.api.AuthApi
import com.example.bookingservice.data.api.AuthResponse
import com.example.bookingservice.data.api.LoginRequest
import com.example.bookingservice.data.api.SignupRequest
import com.example.bookingservice.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

open class AuthRepository(
    private val authApi: AuthApi,
    private val context: Context
) {
    suspend fun login(email: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        .edit()
                        .putString("jwt_token", authResponse.access_token)
                        .apply()
                    Result.success(authResponse.user)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Login failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun signup(username: String, email: String, password: String): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.signup(SignupRequest(username, email, password))
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!
                    context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        .edit()
                        .putString("jwt_token", authResponse.access_token)
                        .apply()
                    Result.success(authResponse.user)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Signup failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun logout(): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authApi.logout()
                if (response.isSuccessful) {
                    context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                        .edit()
                        .remove("jwt_token")
                        .apply()
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Logout failed"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}