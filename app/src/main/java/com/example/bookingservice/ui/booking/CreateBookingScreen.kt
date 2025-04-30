package com.example.bookingservice.ui.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bookingservice.viewmodel.BookingViewModel

@Composable
fun CreateBookingScreen(
    roomId: String,
    viewModel: BookingViewModel = viewModel(),
    onBookingSuccess: () -> Unit
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val bookingSuccess by viewModel.bookingSuccess.collectAsState()

    var userId by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

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

        OutlinedTextField(
            value = userId,
            onValueChange = { userId = it },
            label = { Text("User ID") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = startTime,
            onValueChange = { startTime = it },
            label = { Text("Start Time (e.g., 2023-10-25T10:00:00Z)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = endTime,
            onValueChange = { endTime = it },
            label = { Text("End Time (e.g., 2023-10-25T12:00:00Z)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.createBooking(userId, roomId, startTime, endTime) },
                modifier = Modifier.fillMaxWidth(),
                enabled = userId.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank()
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
}