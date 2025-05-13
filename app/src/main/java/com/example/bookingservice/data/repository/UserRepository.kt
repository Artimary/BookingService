package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.UpdateUserRequest
import com.example.bookingservice.data.api.UserApi
import com.example.bookingservice.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val userApi: UserApi
) {
    suspend fun updateUser(id: String, email: String?, password: String?): Result<User> {
        return withContext(Dispatchers.IO) {
            try {
                val response = userApi.updateUser(id, UpdateUserRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Failed to update user"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
