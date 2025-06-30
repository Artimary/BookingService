package com.example.bookingservice.viewmodel

import app.cash.turbine.test
import com.example.bookingservice.data.model.User
import com.example.bookingservice.data.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
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
    private val mockUserRepository: com.example.bookingservice.data.repository.UserRepository = mockk()
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
        viewModel = AuthViewModel(mockRepository, mockUserRepository)
    }

//    @Test
//    fun `login success should update user state`() = runTest {
//        coEvery { mockRepository.login(any(), any()) } returns Result.success(testUser)
//
//        viewModel.login("email@test.com", "pass")
//        assertTrue(viewModel)
//        runCurrent() // позволяет coroutine завершиться
//
////        assertEquals(testUser, viewModel.user.value)
//        assertFalse(viewModel.isLoading.value)
//    }



//    @Test
//    fun `login failure should update error state`() = runTest {
//        // Arrange
//        val errorMsg = "Invalid credentials"
//        coEvery { mockRepository.login(any(), any()) } returns Result.failure(Exception(errorMsg))
//
//        // Act
//        viewModel.login("wrong", "creds")
//
//        // Явно завершаем все корутины
//        advanceUntilIdle()
//
//        println("User value: ${viewModel.user.value}")
//        println("Error value: ${viewModel.error.value}")
//        // Assert
//        assertEquals(errorMsg, viewModel.error.value)
//        assertNull(viewModel.user.value)
//        assertFalse(viewModel.isLoading.value)
//    }


    @Test
    fun `resetRegistrationSuccess should set flag to false`() = runTest {
        // Arrange
        coEvery { mockRepository.signup(any(), any(), any()) } returns Result.success(testUser)
        viewModel.register("newUser", "email@test.com", "pass")
        assertTrue(viewModel.registrationSuccess.value)

        // Act
        viewModel.resetRegistrationSuccess()

        // Assert
        assertFalse(viewModel.registrationSuccess.value)
    }

//    @Test
//    fun `updatePassword success should reflect in result state`() = runTest {
//        // Arrange
//        coEvery { mockUserRepository.updateUser(any(), null, any()) } returns Result.success(testUser)
//
//        // Act
//        viewModel.updatePassword("123", "newPassword")
//
//        // Явно завершаем все корутины
//        advanceUntilIdle()
//
//        println("User value: ${viewModel.user.value}")
//        println("Error value: ${viewModel.error.value}")
//        // Assert
//        val result = viewModel.updatePasswordResult.value
//        assertNotNull(result)
//        assertTrue(result?.isSuccess == true)
//    }


//    @Test
//    fun `updatePassword failure should reflect in result state`() = runTest {
//        // Arrange
//        val error = Exception("Update failed")
//        coEvery { mockUserRepository.updateUser(any(), null, any()) } returns Result.failure(error)
//
//        // Act
//        viewModel.updatePassword("123", "newPassword")
//
//        // Явно завершаем все корутины
//        advanceUntilIdle()
//
//        println("User value: ${viewModel.user.value}")
//        println("Error value: ${viewModel.error.value}")
//        // Assert
//        val result = viewModel.updatePasswordResult.value
//        assertNotNull(result)
//        assertTrue(result?.isFailure == true)
//        assertEquals(error, result?.exceptionOrNull())
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

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}