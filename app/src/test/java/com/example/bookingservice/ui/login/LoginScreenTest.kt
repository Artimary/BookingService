package com.example.bookingservice.ui.login

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.example.bookingservice.viewmodel.AuthViewModel
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.bookingservice.MainActivity
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

class LoginScreenTest {
    @get:Rule
    val composeTestRule = createComposeRule()

    private val mockViewModel: AuthViewModel = mockk(relaxed = true) {
        every { isLoading } returns MutableStateFlow(false)
        every { error } returns MutableStateFlow(null)
    }

//    @Test
//    fun `should display login screen elements`() {
//        composeTestRule.setContent {
//            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {}, onRegisterClick = {})
//        }
//
//        composeTestRule.onNodeWithTag("LoginButton").assertExists()
//        composeTestRule.onNodeWithTag("UsernameField").assertExists()
//        composeTestRule.onNodeWithTag("PasswordField").assertExists()
//    }
//
//    @Test
//    fun `login button click should trigger viewmodel login`() {
//        composeTestRule.setContent {
//            LoginScreen(viewModel = mockViewModel, onLoginSuccess = {}, onRegisterClick = {})
//        }
//
//        composeTestRule.onNodeWithTag("UsernameField").performTextInput("test@example.com")
//        composeTestRule.onNodeWithTag("PasswordField").performTextInput("password123")
//        composeTestRule.onNodeWithTag("LoginButton").performClick()
//
//        verify(exactly = 1) { mockViewModel.login("test@example.com", "password123") }
//    }
//
//    @Test
//    fun `should show loading indicator during login`() {
//        every { mockViewModel.isLoading } returns MutableStateFlow(true)
//
//        composeTestRule.setContent {
//            LoginScreen(
//                viewModel = mockViewModel,
//                onLoginSuccess = {},
//                onRegisterClick = {}
//            )
//        }
//
//        composeTestRule.onNodeWithTag("LoadingIndicator").assertExists()
//    }
}