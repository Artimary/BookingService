package com.example.bookingservice.ui.building

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.bookingservice.viewmodel.BuildingViewModel
import androidx. compose. ui. tooling. preview. Preview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun BuildingListScreen(
    viewModel: BuildingViewModel,
    onBuildingClick: (String) -> Unit
) {
    val buildings by viewModel.buildings.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadBuildings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Buildings",
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
            buildings == null -> {
                Text("Buildings data not loaded")
            }
            buildings?.isEmpty() == true -> {
                Text("No buildings found")
            }
            buildings != null -> {
                val buildingsList = buildings
                buildingsList?.forEach { building ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        onClick = { onBuildingClick(building.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Name: ${building.name}")
                            Text("Address: ${building.address}")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewBuildingListScreen() {
    // Mock BuildingApi implementation
    class MockBuildingApi : com.example.bookingservice.data.api.BuildingApi {
        override suspend fun createBuilding(request: com.example.bookingservice.data.api.CreateBuildingRequest): retrofit2.Response<com.example.bookingservice.data.model.Building> {
            val building = com.example.bookingservice.data.model.Building("1", request.name, request.address)
            return retrofit2.Response.success(building)
        }

        override suspend fun getBuilding(id: String): retrofit2.Response<com.example.bookingservice.data.model.Building> {
            val building = com.example.bookingservice.data.model.Building(id, "Building $id", "Address $id")
            return retrofit2.Response.success(building)
        }

        override suspend fun getAllBuildings(): retrofit2.Response<List<com.example.bookingservice.data.model.Building>> {
            val buildings = listOf(
                com.example.bookingservice.data.model.Building("1", "Building One", "123 Main St"),
                com.example.bookingservice.data.model.Building("2", "Building Two", "456 Elm St")
            )
            return retrofit2.Response.success(buildings)
        }
    }

    val mockRepository = com.example.bookingservice.data.repository.BuildingRepository(MockBuildingApi())

    val mockViewModel = object : BuildingViewModel(mockRepository) {
        private val _buildings = MutableStateFlow<List<com.example.bookingservice.data.model.Building>?>(listOf(
            com.example.bookingservice.data.model.Building("1", "Building One", "123 Main St"),
            com.example.bookingservice.data.model.Building("2", "Building Two", "456 Elm St")
        ))
        override val buildings = _buildings.asStateFlow()

        private val _isLoading = MutableStateFlow(false)
        override val isLoading = _isLoading.asStateFlow()

        private val _error = MutableStateFlow<String?>(null)
        override val error = _error.asStateFlow()

        override fun loadBuildings() {}
    }

    BuildingListScreen(
        viewModel = mockViewModel,
        onBuildingClick = {}
    )
}
