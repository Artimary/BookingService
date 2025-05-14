package com.example.bookingservice.ui.booking

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookingservice.viewmodel.AuthViewModel
import com.example.bookingservice.viewmodel.BookingViewModel
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.ui.platform.LocalContext
import com.example.bookingservice.data.api.UserApi
import com.example.bookingservice.data.api.UpdateUserRequest
import com.example.bookingservice.data.repository.UserRepository

@Composable
fun BookingListScreen(
    userId: String,
    viewModel: BookingViewModel = viewModel(),
    authViewModel: AuthViewModel, // Добавлен параметр
    onCreateBookingClick: () -> Unit,
    onLogout: () -> Unit // Добавлен параметр
) {
    val bookings by viewModel.bookings.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val authError by authViewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = userId) {
        Log.d("BookingListScreen", "Loading bookings for userId=$userId")
        viewModel.loadBookings(userId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Your Bookings",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                Log.d("BookingListScreen", "Showing loading indicator")
                CircularProgressIndicator()
            }
            error != null -> {
                Log.d("BookingListScreen", "Error: $error")
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            bookings == null -> {
                Log.d("BookingListScreen", "Bookings is null")
                Text("Bookings data not loaded")
            }
            bookings?.isEmpty() == true -> {
                Log.d("BookingListScreen", "Bookings is empty")
                Text("No bookings found for user $userId")
            }
            else -> {
                Log.d("BookingListScreen", "Bookings size=${bookings?.size}")
                bookings?.forEach { booking ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Room: ${booking.roomId}")
                            Text("Time: ${booking.startTime} to ${booking.endTime}")
                            Text("Status: ${booking.status}")
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.deleteBooking(booking.id) },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Delete Booking")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onCreateBookingClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create New Booking")
        }
        Spacer(modifier = Modifier.height(8.dp))
        var showChangePasswordDialog by remember { mutableStateOf(false) }
        Button(
            onClick = { showChangePasswordDialog = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Change Password")
        }
        if (showChangePasswordDialog) {
            var newPassword by remember { mutableStateOf("") }
            AlertDialog(
                onDismissRequest = { showChangePasswordDialog = false },
                title = { Text("Change Password") },
                text = {
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        label = { Text("New Password") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            authViewModel.updatePassword(userId, newPassword)
                            showChangePasswordDialog = false
                        }
                    ) {
                        Text("Confirm")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showChangePasswordDialog = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                authViewModel.logout()
                onLogout()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Logout")
        }

        if (authError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = authError!!,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBookingListScreen() {
    val mockBookings = listOf(
        com.example.bookingservice.data.model.Booking(
            id = "1",
            userId = "user1",
            roomId = "room1",
            startTime = "2024-01-01T10:00",
            endTime = "2024-01-01T11:00",
            status = "Confirmed"
        ),
        com.example.bookingservice.data.model.Booking(
            id = "2",
            userId = "user1",
            roomId = "room2",
            startTime = "2024-01-02T12:00",
            endTime = "2024-01-02T13:00",
            status = "Pending"
        )
    )

    val repository = com.example.bookingservice.data.repository.BookingRepository(
        bookingApi = object : com.example.bookingservice.data.api.BookingApi {
            override suspend fun createBooking(request: com.example.bookingservice.data.api.CreateBookingRequest) = throw NotImplementedError()
            override suspend fun getBooking(id: String) = throw NotImplementedError()
            override suspend fun getAllBookings() = throw NotImplementedError()
            override suspend fun deleteBooking(id: String) = throw NotImplementedError()
        }
    )
    val mockViewModel = object : BookingViewModel(repository) {
        override val bookings: StateFlow<List<com.example.bookingservice.data.model.Booking>?> = MutableStateFlow(mockBookings).asStateFlow()
        override val isLoading: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
        override val error: StateFlow<String?> = MutableStateFlow(null).asStateFlow()
        override fun loadBookings(userId: String) {}
        override fun deleteBooking(id: String) {}
    }

    val mockAuthApi = object : com.example.bookingservice.data.api.AuthApi {
        override suspend fun login(request: com.example.bookingservice.data.api.LoginRequest) = throw NotImplementedError()
        override suspend fun signup(request: com.example.bookingservice.data.api.SignupRequest) = throw NotImplementedError()
        override suspend fun logout() = throw NotImplementedError()
    }

    val mockUserApi = object : com.example.bookingservice.data.api.UserApi {
        override suspend fun updateUser(id: String, request: com.example.bookingservice.data.api.UpdateUserRequest) = throw NotImplementedError()
    }

    val mockContext = LocalContext.current

    val mockAuthRepository = com.example.bookingservice.data.repository.AuthRepository(
        authApi = mockAuthApi,
        context = mockContext
    )

    val mockUserRepository = com.example.bookingservice.data.repository.UserRepository(mockUserApi)

    val mockAuthViewModel = object : AuthViewModel(mockAuthRepository, mockUserRepository) {
        override val error: StateFlow<String?> = MutableStateFlow(null).asStateFlow()
        override fun logout() {}
    }

    BookingListScreen(
        userId = "user1",
        viewModel = mockViewModel,
        authViewModel = mockAuthViewModel,
        onCreateBookingClick = {},
        onLogout = {}
    )
}
