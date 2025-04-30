package com.example.bookingservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingservice.data.model.Booking
import com.example.bookingservice.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class BookingViewModel(
    private val repository: BookingRepository
) : ViewModel() {
    private val _bookings = MutableStateFlow<List<Booking>?>(null)
    val bookings: StateFlow<List<Booking>?> = _bookings

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _bookingSuccess = MutableStateFlow(false)
    val bookingSuccess: StateFlow<Boolean> = _bookingSuccess

    fun loadBookings(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getBookings(userId)
            _isLoading.value = false
            if (result.isSuccess) {
                _bookings.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to load bookings"
            }
        }
    }

    fun createBooking(userId: String, roomId: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.createBooking(userId, roomId, startTime, endTime)
            _isLoading.value = false
            if (result.isSuccess) {
                _bookingSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to create booking"
            }
        }
    }

    fun resetBookingSuccess() {
        _bookingSuccess.value = false
    }
}