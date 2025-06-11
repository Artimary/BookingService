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
    val user_id: String,
    @Field("room_id")
    val room_id: String,
    @Field("start_time")
    val start_time: String,
    @Field("end_time")
    val end_time: String,
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
    suspend fun deleteBooking(@Path("id") id: String): Response<Unit>
}
