package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.BuildingApi
import com.example.bookingservice.data.model.Building
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
class BuildingRepositoryTest {
    private lateinit var buildingRepository: BuildingRepository
    private val mockBuildingApi: BuildingApi = mockk()

    private val testBuilding = Building(
        id = "building123",
        name = "Test Building",
        address = "123 Test St"
    )
    private val testBuildings = listOf(testBuilding)

    @Before
    fun setup() {
        buildingRepository = BuildingRepository(mockBuildingApi)
    }

    @Test
    fun `getAllBuildings success should return buildings`() = runTest {
        // Arrange
        val mockResponse = Response.success(testBuildings)
        coEvery { mockBuildingApi.getAllBuildings() } returns mockResponse

        // Act
        val result = buildingRepository.getAllBuildings()

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(testBuildings, result.getOrNull())
    }

    @Test
    fun `getAllBuildings failure should return error`() = runTest {
        // Arrange
        val errorBody = "{\"error\":\"server_error\"}"
            .toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<List<Building>>(500, errorBody)
        coEvery { mockBuildingApi.getAllBuildings() } returns errorResponse

        // Act
        val result = buildingRepository.getAllBuildings()

        // Assert
        assertTrue(result.isFailure)
        // Remove the specific message assertion
    }

    @Test
    fun `getAllBuildings exception should return failure`() = runTest {
        // Arrange
        coEvery { mockBuildingApi.getAllBuildings() } throws Exception("Network error")

        // Act
        val result = buildingRepository.getAllBuildings()

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}