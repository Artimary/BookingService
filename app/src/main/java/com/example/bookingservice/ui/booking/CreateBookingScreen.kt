package com.example.bookingservice.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookingservice.viewmodel.BookingViewModel
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

import androidx.compose.runtime.collectAsState
import com.example.bookingservice.data.api.BookingApi
import com.example.bookingservice.data.api.CreateBookingRequest
import com.example.bookingservice.data.repository.BookingRepository
import com.example.bookingservice.viewmodel.AuthViewModel
import com.example.bookingservice.data.api.UserApi
import com.example.bookingservice.data.api.UpdateUserRequest
import com.example.bookingservice.data.repository.UserRepository

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset
import androidx.compose.ui.platform.LocalContext
import android.app.TimePickerDialog
import android.app.DatePickerDialog
import java.util.Calendar

@Composable
fun CreateBookingScreen(
    roomId: String,
    viewModel: BookingViewModel,
    authViewModel: AuthViewModel,
    onBookingSuccess: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val bookingSuccess by viewModel.bookingSuccess.collectAsState()
    val user by authViewModel.user.collectAsState()

    // Состояния для выбора даты и времени
    var startDate by remember { mutableStateOf<LocalDateTime?>(null) }
    var endDate by remember { mutableStateOf<LocalDateTime?>(null) }

    // Состояния для отображения диалогов выбора даты и времени
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isSelectingStart by remember { mutableStateOf(true) }
    var isSelectingEndAfterStart by remember { mutableStateOf(false) }
    var isSelectingEndAfterTimeStart by remember { mutableStateOf(false) }

    val calendar = Calendar.getInstance()

    val context = LocalContext.current

    if (bookingSuccess) {
        AlertDialog(
            onDismissRequest = {
                viewModel.resetBookingSuccess()
                onBookingSuccess()
            },
            title = { Text("Booking Created") },
            text = { Text("Your booking has been successfully created.") },
            confirmButton = {
                Button(onClick = {
                    viewModel.resetBookingSuccess()
                    onBookingSuccess()
                }) {
                    Text("OK")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Create Booking",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "User ID: ${user?.id ?: "Unknown"}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            isSelectingStart = true
            isSelectingEndAfterStart = true
            showDatePicker = true
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Select Date")
        }
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            isSelectingStart = true
            isSelectingEndAfterTimeStart = true
            showTimePicker = true
        }, modifier = Modifier.fillMaxWidth()) {
            Text(text = "Select Time")
        }
        Spacer(modifier = Modifier.height(16.dp))

        val userId = user?.id ?: ""

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            // Преобразуем LocalDateTime в ISO 8601 строку с зоной UTC
            val startTimeIso = startDate?.atZone(ZoneOffset.systemDefault())?.withZoneSameInstant(ZoneOffset.UTC)?.format(DateTimeFormatter.ISO_INSTANT) ?: ""
            val endTimeIso = endDate?.atZone(ZoneOffset.systemDefault())?.withZoneSameInstant(ZoneOffset.UTC)?.format(DateTimeFormatter.ISO_INSTANT) ?: ""

            Button(
                onClick = { viewModel.createBooking(userId, roomId, startTimeIso, endTimeIso) },
                modifier = Modifier.fillMaxWidth(),
                enabled = userId.isNotBlank() && startTimeIso.isNotBlank() && endTimeIso.isNotBlank()
            ) {
                Text("Create Booking")
            }
        }

        error?.let {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }

    // Диалог выбора даты
    if (showDatePicker) {
        val year = if (isSelectingStart) startDate?.year ?: calendar.get(Calendar.YEAR) else endDate?.year ?: calendar.get(Calendar.YEAR)
        val month = if (isSelectingStart) startDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH) else endDate?.monthValue?.minus(1) ?: calendar.get(Calendar.MONTH)
        val day = if (isSelectingStart) startDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH) else endDate?.dayOfMonth ?: calendar.get(Calendar.DAY_OF_MONTH)

        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val time = if (isSelectingStart) startDate?.toLocalTime() ?: java.time.LocalTime.MIDNIGHT else endDate?.toLocalTime() ?: java.time.LocalTime.MIDNIGHT
                val selectedDateTime = LocalDateTime.of(selectedYear, selectedMonth + 1, selectedDayOfMonth, time.hour, time.minute)
                if (isSelectingStart) {
                    startDate = selectedDateTime
                    if (isSelectingEndAfterStart) {
                        isSelectingStart = false
                        showDatePicker = true
                        isSelectingEndAfterStart = false
                    } else {
                        showDatePicker = false
                    }
                } else {
                    endDate = selectedDateTime
                    showDatePicker = false
                }
            },
            year,
            month,
            day
        ).show()
    }

    // Диалог выбора времени
    if (showTimePicker) {
        val hour = if (isSelectingStart) startDate?.hour ?: calendar.get(Calendar.HOUR_OF_DAY) else endDate?.hour ?: calendar.get(Calendar.HOUR_OF_DAY)
        val minute = if (isSelectingStart) startDate?.minute ?: calendar.get(Calendar.MINUTE) else endDate?.minute ?: calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, selectedHour, selectedMinute ->
                val date = if (isSelectingStart) startDate?.toLocalDate() ?: java.time.LocalDate.now() else endDate?.toLocalDate() ?: java.time.LocalDate.now()
                val selectedDateTime = LocalDateTime.of(date, java.time.LocalTime.of(selectedHour, selectedMinute))
                if (isSelectingStart) {
                    startDate = selectedDateTime
                    if (isSelectingEndAfterTimeStart) {
                        isSelectingStart = false
                        showTimePicker = true
                        isSelectingEndAfterTimeStart = false
                    } else {
                        showTimePicker = false
                    }
                } else {
                    endDate = selectedDateTime
                    showTimePicker = false
                }
            },
            hour,
            minute,
            true
        ).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateBookingScreen() {
    val mockViewModel = object : BookingViewModel(repository = BookingRepository(
        bookingApi = object : BookingApi {
            override suspend fun createBooking(request: CreateBookingRequest) = throw NotImplementedError()
            override suspend fun getBooking(id: String) = throw NotImplementedError()
            override suspend fun getAllBookings() = throw NotImplementedError()
            override suspend fun deleteBooking(id: String) = throw NotImplementedError()
        }
    )) {
        override val isLoading: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
        override val error: StateFlow<String?> = MutableStateFlow(null).asStateFlow()
        override val bookingSuccess: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
        override fun createBooking(userId: String, roomId: String, startTime: String, endTime: String) {}
    }

    val context = androidx.compose.ui.platform.LocalContext.current

    val mockAuthRepository = object : com.example.bookingservice.data.repository.AuthRepository(
        authApi = object : com.example.bookingservice.data.api.AuthApi {
            override suspend fun login(request: com.example.bookingservice.data.api.LoginRequest) = throw NotImplementedError()
            override suspend fun signup(request: com.example.bookingservice.data.api.SignupRequest) = throw NotImplementedError()
            override suspend fun logout() = throw NotImplementedError()
        },
        context = context
    ) {}

    val mockUserApi = object : com.example.bookingservice.data.api.UserApi {
        override suspend fun updateUser(id: String, request: com.example.bookingservice.data.api.UpdateUserRequest) = throw NotImplementedError()
    }

    val mockUserRepository = com.example.bookingservice.data.repository.UserRepository(mockUserApi)

    val mockAuthViewModel = object : AuthViewModel(mockAuthRepository, mockUserRepository) {
        override val error: StateFlow<String?> = MutableStateFlow(null).asStateFlow()
        override fun logout() {}
    }

    CreateBookingScreen(
        roomId = "room1",
        viewModel = mockViewModel,
        authViewModel = mockAuthViewModel,
        onBookingSuccess = {}
    )
}
