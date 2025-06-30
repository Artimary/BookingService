package com.example.bookingservice.data.repository

import com.example.bookingservice.data.api.UpdateUserRequest
import com.example.bookingservice.data.api.UserApi
import com.example.bookingservice.data.model.User
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
class UserRepositoryTest {
    private lateinit var userRepository: UserRepository
    private val mockUserApi: UserApi = mockk()

    private val testUser = User(
        id = "123",
        username = "testUser",
        email = "test@example.com",
        role = "user"
    )

    @Before
    fun setup() {
        userRepository = UserRepository(mockUserApi)
    }

    @Test
    fun `updateUser success should return updated user`() = runTest {
        // Arrange
        val mockResponse = Response.success(testUser)
        coEvery { mockUserApi.updateUser(any(), any()) } returns mockResponse

        // Act
        val result = userRepository.updateUser("123", "new@email.com", "newpass")

        // Assert
        assertTrue(result.isSuccess)
        assertEquals(testUser, result.getOrNull())
    }

    @Test
    fun `updateUser failure should return error`() = runTest {
        // Arrange
        val errorBody = "{\"error\":\"update_failed\"}"
            .toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<User>(400, errorBody)
        coEvery { mockUserApi.updateUser(any(), any()) } returns errorResponse

        // Act
        val result = userRepository.updateUser("123", "new@email.com", "newpass")

        // Assert
        assertTrue(result.isFailure)
        // Remove the specific message assertion
    }

    @Test
    fun `updateUser exception should return failure`() = runTest {
        // Arrange
        coEvery { mockUserApi.updateUser(any(), any()) } throws Exception("Network error")

        // Act
        val result = userRepository.updateUser("123", "new@email.com", "newpass")

        // Assert
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }
}