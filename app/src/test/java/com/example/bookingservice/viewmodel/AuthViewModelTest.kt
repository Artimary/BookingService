package com.example.bookingservice.viewmodel

import com.example.bookingservice.data.model.User
import com.example.bookingservice.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthViewModelTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockRepository: AuthRepository = mockk()
    private lateinit var viewModel: AuthViewModel

    private val testUser = User(
        id = "123",
        username = "testUser",
        email = "test@example.com",
        role = "user"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AuthViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

//    @Test
//    fun `login success should update user state`() = runTest {
//        // Arrange
//        coEvery { mockRepository.login(any(), any()) } returns Result.success(testUser)
//
//        // Act
//        viewModel.login("test", "pass")
//
//        advanceUntilIdle()
//
//        // Assert
//        assertEquals(testUser, viewModel.user.value)
//        assertFalse(viewModel.isLoading.value)
//    }
//
//    @Test
//    fun `login failure should update error state`() = runTest {
//        // Arrange
//        val errorMsg = "Invalid credentials"
//        coEvery { mockRepository.login(any(), any()) } returns Result.failure(Exception(errorMsg))
//
//        // Act
//        viewModel.login("wrong", "creds")
//
//        // Assert
//        advanceUntilIdle() // Ожидаем завершения корутин
//
//        assertEquals(errorMsg, viewModel.error.value)
//        assertNull(viewModel.user.value)
//    }

    @Test
    fun `registration success should update registration flag`() = runTest {
        // Arrange
        coEvery { mockRepository.signup(any(), any(), any()) } returns Result.success(testUser)

        // Act
        viewModel.register("newUser", "email@test.com", "pass")

        // Assert
        assertTrue(viewModel.registrationSuccess.value)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `logout should clear user state`() = runTest {
        // Arrange
        coEvery { mockRepository.logout() } returns Result.success(Unit)

        // Act
        viewModel.logout()

        advanceUntilIdle()

        // Assert
        assertNull(viewModel.user.value)
    }
}