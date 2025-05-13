package com.example.bookingservice.data.api

import com.example.bookingservice.data.model.Booking
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.DELETE

data class CreateBookingRequest(
    @Field("user_id")
    val userId: String,
    @Field("room_id")
    val roomId: String,
    @Field("start_time")
    val startTime: String,
    @Field("end_time")
    val endTime: String, 
    val status: String? = null
)

interface BookingApi {
    @POST("bookings/")
    suspend fun createBooking(@Body request: CreateBookingRequest): Response<Booking>

    @GET("bookings/{id}")
    suspend fun getBooking(@Path("id") id: String): Response<Booking>

    @GET("bookings/all")
    suspend fun getAllBookings(): Response<List<Booking>>

    @DELETE("bookings/{id}")
    suspend fun deleteBooking(@Path("id") id: String): Response<Booking>
}
