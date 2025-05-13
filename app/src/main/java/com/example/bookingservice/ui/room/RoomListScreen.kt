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
import androidx. compose. ui. tooling. preview. Preview
import com.example.bookingservice.data.model.Room
import com.example.bookingservice.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


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

@Preview(showBackground = true)
@Composable
fun PreviewRoomListScreen() {
    // Mok-данные для списка комнат
    val mockRooms = listOf(
        Room(id = "1", buildingId = "1", name = "Room 1", capacity = 10, type = "Meeting", status = "Available"),
        Room(id = "2", buildingId = "1" , name = "Room 2", capacity = 20, type = "Conference", status = "Occupied")
    )

    // Мок-объект RoomApi с пустыми реализациями
    val mockRoomApi = object : com.example.bookingservice.data.api.RoomApi {
        override suspend fun createRoom(request: com.example.bookingservice.data.api.CreateRoomRequest) = throw NotImplementedError()
        override suspend fun getRoom(id: String) = throw NotImplementedError()
        override suspend fun getRoomsByBuilding(buildingId: String) = throw NotImplementedError()
        override suspend fun getAllRooms() = throw NotImplementedError()
    }

    // Мок-репозиторий с мок-объектом RoomApi
    val mockRepository = com.example.bookingservice.data.repository.RoomRepository(mockRoomApi)

    // Mok-ViewModel с предопределёнными состояниями
    val mockViewModel = object : RoomViewModel(mockRepository) {
        override val rooms: StateFlow<List<Room>> = MutableStateFlow(mockRooms).asStateFlow()
        override val isLoading: StateFlow<Boolean> = MutableStateFlow(false).asStateFlow()
        override val error: StateFlow<String?> = MutableStateFlow(null).asStateFlow()
        override fun loadRooms(buildingId: String) {
            // Пустая реализация для предосмотра
        }
    }

    // Вызов RoomListScreen с mok-данными
    RoomListScreen(
        buildingId = "mockBuildingId",
        viewModel = mockViewModel,
        onRoomClick = { /* Пустая функция для предосмотра */ }
    )
}
