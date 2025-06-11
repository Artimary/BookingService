package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.BookingApi
import com.example.bookingservice.data.api.CreateBookingRequest
import com.example.bookingservice.data.model.Booking
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookingRepository(
    private val bookingApi: BookingApi
) {
    suspend fun getBookings(userId: String): Result<List<Booking>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingApi.getAllBookings()
                if (response.isSuccessful && response.body() != null) {
                    val bookings = response.body()!!.filter { it.user_id == userId }
                    Result.success(bookings)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Failed to load bookings"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun createBooking(
        userId: String,
        roomId: String,
        startTime: String,
        endTime: String
    ): Result<Booking> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingApi.createBooking(
                    CreateBookingRequest(userId, roomId, startTime.substring(startIndex = 0, endIndex = startTime.length-1), endTime.substring(startIndex = 0, endIndex = endTime.length-1))
                )
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Failed to create booking"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun deleteBooking(id: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val response = bookingApi.deleteBooking(id)
                if (response.isSuccessful && response.body() != null) {
                    Result.success(response.body()!!)
                } else {
                    Result.failure(Exception(response.errorBody()?.string() ?: "Failed to delete booking"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }
}
