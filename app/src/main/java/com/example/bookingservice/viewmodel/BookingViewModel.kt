package com.example.bookingservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingservice.data.model.Booking
import com.example.bookingservice.data.repository.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

open class BookingViewModel(
    private val repository: BookingRepository
) : ViewModel() {
    private val _bookings = MutableStateFlow<List<Booking>?>(null)
    open val bookings: StateFlow<List<Booking>?> = _bookings

    private val _isLoading = MutableStateFlow(false)
    open val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    open val error: StateFlow<String?> = _error

    private val _bookingSuccess = MutableStateFlow(false)
    open val bookingSuccess: StateFlow<Boolean> = _bookingSuccess

    private var currentUserId: String? = null

    open fun loadBookings(userId: String) {
        currentUserId = userId
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.getBookings(userId)
            if (result.isSuccess) {
                _bookings.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to load bookings"
            }
            _isLoading.value = false
        }
    }

    open fun createBooking(userId: String, roomId: String, startTime: String, endTime: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.createBooking(userId, roomId, startTime, endTime)
            if (result.isSuccess) {
                _bookingSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to create booking"
            }
            _isLoading.value = false
        }
    }

    fun resetBookingSuccess() {
        _bookingSuccess.value = false
    }

    open fun deleteBooking(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.deleteBooking(id)
            if (result.isSuccess) {
                currentUserId?.let { loadBookings(it) }
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Failed to delete booking"
            }
            _isLoading.value = false
        }
    }
}