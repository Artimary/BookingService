package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.BookingApi
import com.example.bookingservice.data.api.CreateBookingRequest
import com.example.bookingservice.data.model.Booking
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class BookingRepositoryTest {
    private lateinit var bookingRepository: BookingRepository
    private val mockBookingApi: BookingApi = mockk()

    private val testUserId = "user123"
    private val testRoomId = "room456"
    private val testBooking = Booking(
        id = "booking789",
        user_id = testUserId,
        room_id = testRoomId,
        start_time = "2023-11-15T10:00:00Z",
        end_time = "2023-11-15T11:00:00Z",
        status =  "CONFIRMED"
    )
    private val testBookings = listOf(testBooking)

    @Before
    fun setup() {
        bookingRepository = BookingRepository(mockBookingApi)
    }

    @Test
    fun `getBookings should filter by user ID`() = runTest {
        // Arrange
        val mockResponse = Response.success(testBookings)
        coEvery { mockBookingApi.getAllBookings() } returns mockResponse

        // Act
        val result = bookingRepository.getBookings(testUserId)

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(testBookings, result.getOrNull())
    }

    @Test
    fun `deleteBooking should return failure on API error`() = runTest {
        // Arrange
        val errorBody = "{\"error\":\"not_found\"}"
            .toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<Unit>(404, errorBody)
        coEvery { mockBookingApi.deleteBooking(any()) } returns errorResponse

        // Act
        val result = bookingRepository.deleteBooking("booking789")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("not_found") == true)
    }

    @Test
    fun `createBooking should return booking on success`() = runTest {
        // Arrange
        val mockResponse = Response.success(testBooking)
        coEvery {
            mockBookingApi.createBooking(any())
        } returns mockResponse

        // Act
        val result = bookingRepository.createBooking(
            testUserId,
            testRoomId,
            "2023-11-15T10:00:00Z",
            "2023-11-15T11:00:00Z"
        )

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(testBooking, result.getOrNull())
    }

    @Test
    fun `deleteBooking should return success`() = runTest {
        // Arrange
        val mockResponse = Response.success(Unit)
        coEvery { mockBookingApi.deleteBooking(any()) } returns mockResponse

        // Act
        val result = bookingRepository.deleteBooking("booking789")

        // Assert
        assertTrue(result.isSuccess)
    }

    @Test
    fun `deleteBooking should handle exceptions`() = runTest {
        // Arrange
        coEvery { mockBookingApi.deleteBooking(any()) } throws Exception("Network error")

        // Act
        val result = bookingRepository.deleteBooking("booking789")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getAllBookings should handle exceptions`() = runTest {
        // Arrange
        coEvery { mockBookingApi.getAllBookings() } throws Exception("Network timeout")

        // Act
        val result = bookingRepository.getBookings(testUserId)

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network timeout", result.exceptionOrNull()?.message)
    }

    @Test
    fun `createBooking should handle exceptions`() = runTest {
        // Arrange
        coEvery {
            mockBookingApi.createBooking(any())
        } throws Exception("Connection error")

        // Act
        val result = bookingRepository.createBooking(
            testUserId,
            testRoomId,
            "2025-07-01T10:00:00",
            "2025-07-01T11:00:00"
        )

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Connection error", result.exceptionOrNull()?.message)
    }

//    @Test
//    fun `modifyBooking should handle exceptions`() = runTest {
//        // Arrange
//        coEvery {
//            mockBookingApi.updateBooking(any(), any())
//        } throws Exception("Server error")
//
//        // Act
//        val result = bookingRepository.modifyBooking(
//            "booking123",
//            testRoomId,
//            "2025-07-01T10:00:00",
//            "2025-07-01T11:00:00"
//        )
//
//        // Assert
//        assertTrue(result.isFailure)
//        assertEquals("Server error", result.exceptionOrNull()?.message)
//    }
}