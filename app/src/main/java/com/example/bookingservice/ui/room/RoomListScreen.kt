package com.example.bookingservice.ui.room

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookingservice.viewmodel.RoomViewModel

@Composable
fun RoomListScreen(
    buildingId: String,
    viewModel: RoomViewModel = viewModel(),
    onRoomClick: (String) -> Unit
) {
    val rooms by viewModel.rooms.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(buildingId) {
        viewModel.loadRooms(buildingId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Rooms",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text(
                    text = error!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            rooms == null -> {
                Text("Rooms data not loaded")
            }
            rooms?.isEmpty() == true -> {
                Text("No rooms found in this building")
            }
            else -> {
                rooms?.forEach { room ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onRoomClick(room.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${room.name}")
                            Text("Capacity: ${room.capacity}")
                            Text("Type: ${room.type}")
                            Text("Status: ${room.status}")
                        }
                    }
                }
            }
        }
    }
}