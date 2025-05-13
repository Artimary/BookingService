package com.example.bookingservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingservice.data.model.Building
import com.example.bookingservice.data.repository.BuildingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BuildingViewModel(
    private val repository: BuildingRepository
) : ViewModel() {
    private val _buildings = MutableStateFlow<List<Building>?>(null)
    open val buildings: StateFlow<List<Building>?> = _buildings

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    open val error: StateFlow<String?> = _error

    open fun loadBuildings() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getAllBuildings()
            _isLoading.value = false
            if (result.isSuccess) {
                _buildings.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to load buildings"
            }
        }
    }
}