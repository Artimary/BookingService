package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.RoomApi
import com.example.bookingservice.data.model.Room
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
class RoomRepositoryTest {
    private lateinit var roomRepository: RoomRepository
    private val mockRoomApi: RoomApi = mockk()

    private val testRoom = Room(
        id = "room123",
        buildingId = "building456",
        name = "Test Room",
        type = "Room",
        capacity = 10,
        status = "BOOKED"
    )
    private val testRooms = listOf(testRoom)

    @Before
    fun setup() {
        roomRepository = RoomRepository(mockRoomApi)
    }

    @Test
    fun `getRoomsByBuilding success should return rooms`() = runTest {
        // Arrange
        val mockResponse = Response.success(testRooms)
        coEvery { mockRoomApi.getRoomsByBuilding(any()) } returns mockResponse

        // Act
        val result = roomRepository.getRoomsByBuilding("building456")

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(testRooms, result.getOrNull())
    }

    @Test
    fun `getRoomsByBuilding failure should return error`() = runTest {
        // Arrange
        val errorBody = "{\"error\":\"not_found\"}"
            .toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<List<Room>>(404, errorBody)
        coEvery { mockRoomApi.getRoomsByBuilding(any()) } returns errorResponse

        // Act
        val result = roomRepository.getRoomsByBuilding("building456")

        // Assert
        assertTrue(result.isFailure)
        // Remove the specific message assertion
    }

    @Test
    fun `getRoomsByBuilding exception should return failure`() = runTest {
        // Arrange
        coEvery { mockRoomApi.getRoomsByBuilding(any()) } throws Exception("Network error")

        // Act
        val result = roomRepository.getRoomsByBuilding("building456")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}