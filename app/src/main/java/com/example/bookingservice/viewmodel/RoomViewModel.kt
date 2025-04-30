package com.example.bookingservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingservice.data.model.Room
import com.example.bookingservice.data.repository.RoomRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RoomViewModel(
    private val repository: RoomRepository
) : ViewModel() {
    private val _rooms = MutableStateFlow<List<Room>?>(null)
    val rooms: StateFlow<List<Room>?> = _rooms

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadRooms(buildingId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getRoomsByBuilding(buildingId)
            _isLoading.value = false
            if (result.isSuccess) {
                _rooms.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to load rooms"
            }
        }
    }
}