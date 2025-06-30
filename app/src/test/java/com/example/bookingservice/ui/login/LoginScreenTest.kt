//package com.example.bookingservice.ui.login
//
//import androidx.compose.ui.test.*
//import androidx.compose.ui.test.junit4.createComposeRule
//import com.example.bookingservice.viewmodel.AuthViewModel
//import com.example.bookingservice.viewmodel.AuthViewModel_test
//import kotlinx.coroutines.flow.MutableStateFlow
//import org.junit.Before
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.mockito.Mockito.mock
//import org.mockito.Mockito.`when`
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.annotation.Config
//
//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [30])
//class LoginScreenTest {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    private lateinit var mockViewModel: AuthViewModel
//    private lateinit var mockTestViewModel: AuthViewModel_test
//
//    // Define StateFlows for the properties we need to mock
//    private lateinit var emailStateFlow: MutableStateFlow<String>
//    private lateinit var passwordStateFlow: MutableStateFlow<String>
//    private lateinit var errorStateFlow: MutableStateFlow<String?>
//    private lateinit var loadingStateFlow: MutableStateFlow<Boolean>
//    private lateinit var authStateFlow: MutableStateFlow<Boolean>
//
//    @Before
//    fun setUp() {
//        // Initialize StateFlows
//        emailStateFlow = MutableStateFlow("")
//        passwordStateFlow = MutableStateFlow("")
//        errorStateFlow = MutableStateFlow(null)
//        loadingStateFlow = MutableStateFlow(false)
//        authStateFlow = MutableStateFlow(false)
//
//        // Initialize mocks
//        mockViewModel = mock(AuthViewModel::class.java)
//        mockTestViewModel = mock(AuthViewModel_test::class.java)
//
//        // Set up the mocks
////        `when`(mockViewModel.emailState).thenReturn(emailStateFlow)
////        `when`(mockViewModel.passwordState).thenReturn(passwordStateFlow)
////        `when`(mockViewModel.error).thenReturn(errorStateFlow)
////        `when`(mockViewModel.loading).thenReturn(loadingStateFlow)
////        `when`(mockViewModel.authenticated).thenReturn(authStateFlow)
//    }
//
//    @Test
//    fun loginScreen_initialState_hasCorrectElements() {
//        // Setup
//        composeTestRule.setContent {
//            LoginScreen(
//                viewModel = mockViewModel,
//                viewModel_test = mockTestViewModel,
//                onLoginSuccess = {},
//                onRegisterClick = {}
//            )
//        }
//
//        // Verify UI elements exist
//        composeTestRule.onNodeWithText("Login", useUnmergedTree = true).assertExists()
//        composeTestRule.onNode(hasSetTextAction()).assertExists()
//    }
//
//    @Test
//    fun loginButton_isDisabled_whenFieldsAreEmpty() {
//        // Setup
//        composeTestRule.setContent {
//            LoginScreen(
//                viewModel = mockViewModel,
//                viewModel_test = mockTestViewModel,
//                onLoginSuccess = {},
//                onRegisterClick = {}
//            )
//        }
//
//        // Find login button and verify it's disabled
//        composeTestRule.onNodeWithText("Sign In", useUnmergedTree = true).assertIsNotEnabled()
//    }
//
//    @Test
//    fun loginButton_isEnabled_whenFieldsAreFilled() {
//        // Setup
//        emailStateFlow.value = "user@example.com"
//        passwordStateFlow.value = "password123"
//
//        composeTestRule.setContent {
//            LoginScreen(
//                viewModel = mockViewModel,
//                viewModel_test = mockTestViewModel,
//                onLoginSuccess = {},
//                onRegisterClick = {}
//            )
//        }
//
//        // Verify button is enabled
//        composeTestRule.onNodeWithText("Sign In", useUnmergedTree = true).assertIsEnabled()
//    }
//
//    @Test
//    fun errorMessage_isDisplayed_whenErrorOccurs() {
//        // Set error state
//        val errorMessage = "Invalid credentials"
//        errorStateFlow.value = errorMessage
//
//        composeTestRule.setContent {
//            LoginScreen(
//                viewModel = mockViewModel,
//                viewModel_test = mockTestViewModel,
//                onLoginSuccess = {},
//                onRegisterClick = {}
//            )
//        }
//
//        // Verify error is displayed
//        composeTestRule.onNodeWithText(errorMessage, useUnmergedTree = true).assertIsDisplayed()
//    }
//}