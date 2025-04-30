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

@Composable
fun BuildingListScreen(
    viewModel: BuildingViewModel = viewModel(),
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
            else -> {
                buildings?.forEach { building ->
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