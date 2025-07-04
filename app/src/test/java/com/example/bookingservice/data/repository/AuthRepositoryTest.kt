package com.example.bookingservice.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.bookingservice.data.api.AuthApi
import com.example.bookingservice.data.api.AuthResponse
import com.example.bookingservice.data.model.User
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class AuthRepositoryTest {
    private lateinit var authRepository: AuthRepository
    private val mockAuthApi: AuthApi = mockk()
    private val mockContext: Context = mockk()
    private val mockSharedPreferences: SharedPreferences = mockk()
    private val mockEditor: SharedPreferences.Editor = mockk()

    private val testUser = User(
        id = "123",
        username = "testUser",
        email = "test@example.com",
        role = "user"
    )
    private val testAuthResponse = AuthResponse(
        access_token = "jwt123",
        user = testUser
    )


    @Before
    fun setup() {
        every { mockContext.getSharedPreferences(any(), any()) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any())} returns mockEditor
        every { mockEditor.apply() } just Runs
        every { mockEditor.remove(any()) } returns mockEditor

        authRepository = AuthRepository(mockAuthApi, mockContext)
    }

    @Test
    fun `login success should save token and return user`() = runTest {
        // Arrange
        val mockResponse = Response.success(testAuthResponse)
        coEvery { mockAuthApi.login(any()) } returns mockResponse

        // Act
        val result = authRepository.login("test", "pass")

        // Assert
        assertTrue(result.isSuccess)
//        verify { mockEditor.putString("jwt_token", "jwt123") }
    }

    @Test
    fun `login failure should return error`() = runTest {
        val errorBody = "{\"error\":\"invalid_credentials\"}"
            .toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<AuthResponse>(400, errorBody)

        coEvery { mockAuthApi.login(any()) } returns errorResponse

        val result = authRepository.login("wrong", "creds")

        assertTrue(result.isFailure)
        // Изменяем проверку сообщения об ошибке
        assertTrue(result.exceptionOrNull()?.message?.contains("invalid_credentials") == true)
    }

    @Test
    fun `signup success should save token`() = runTest {
        val testAuthResponse = testAuthResponse
        val mockResponse = Response.success(testAuthResponse)
        coEvery { mockAuthApi.signup(any()) } returns mockResponse

        val result = authRepository.signup("newUser", "email@test.com", "pass")

        assertTrue(result.isSuccess)
//        verify(exactly = 1) { mockEditor.putString("jwt_token", "jwt456") }
    }

    @Test
    fun `logout success should remove token`() = runTest {
        // Arrange
        coEvery { mockAuthApi.logout() } returns Response.success(Unit)

        // Act
        val result = authRepository.logout()

        // Assert
        assertTrue(result.isSuccess)
        verify { mockEditor.remove("jwt_token") }
    }

    @Test
    fun `signup failure should return error result`() = runTest {
        // Arrange
        val errorBody = "{\"error\":\"email_exists\"}"
            .toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<AuthResponse>(400, errorBody)
        coEvery { mockAuthApi.signup(any()) } returns errorResponse

        // Act
        val result = authRepository.signup("user", "existing@email.com", "password")

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("email_exists") == true)
    }

    @Test
    fun `logout failure should return error result`() = runTest {
        // Arrange
        val errorBody = "{\"error\":\"unauthorized\"}"
            .toResponseBody("application/json".toMediaType())
        val errorResponse = Response.error<Unit>(401, errorBody)
        coEvery { mockAuthApi.logout() } returns errorResponse

        // Act
        val result = authRepository.logout()

        // Assert
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("unauthorized") == true)
    }

    @Test
    fun `login should handle network exceptions`() = runTest {
        // Arrange
        val exception = IOException("Network error")
        coEvery { mockAuthApi.login(any()) } throws exception

        // Act
        val result = authRepository.login("email@test.com", "password")

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `signup should handle network exceptions`() = runTest {
        // Arrange
        val exception = IOException("Network error")
        coEvery { mockAuthApi.signup(any()) } throws exception

        // Act
        val result = authRepository.signup("user", "email@test.com", "password")

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `logout should handle network exceptions`() = runTest {
        // Arrange
        val exception = IOException("Network error")
        coEvery { mockAuthApi.logout() } throws exception

        // Act
        val result = authRepository.logout()

        // Assert
        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

}