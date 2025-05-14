package com.example.bookingservice.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingservice.data.model.User
import com.example.bookingservice.data.repository.AuthRepository
import com.example.bookingservice.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import com.example.bookingservice.data.api.AuthResponse

open class AuthViewModel(
    private val repository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    open val error: StateFlow<String?> = _error.asStateFlow()

    private val _registrationSuccess = MutableStateFlow(false)
    val registrationSuccess: StateFlow<Boolean> = _registrationSuccess.asStateFlow()

    private val _updatePasswordResult = MutableStateFlow<Result<Unit>?>(null)
    val updatePasswordResult: StateFlow<Result<Unit>?> = _updatePasswordResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            Log.d("AuthViewModel", "Starting login request with email=$email")
            try {
                val result = repository.login(email, password)
                _isLoading.value = false
                if (result.isSuccess) {
                    _user.value = result.getOrThrow() as User?
                    Log.d("AuthViewModel", "Login successful: ${_user.value}")
                } else {
                    val errorMsg = result.exceptionOrNull()?.stackTraceToString() ?: "Unknown error"
                    _error.value = errorMsg
                    Log.d("AuthViewModel", "Login failed: $errorMsg")
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = e.message ?: "Unexpected error"
                Log.e("AuthViewModel", "Login exception: ${e.stackTraceToString()}")
            }
        }
    }

    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.signup(username, email, password)
            _isLoading.value = false
            if (result.isSuccess) {
                _registrationSuccess.value = true
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Registration failed"
            }
        }
    }

    fun resetRegistrationSuccess() {
        _registrationSuccess.value = false
    }

    open fun logout() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            val result = repository.logout()
            _isLoading.value = false
            if (result.isSuccess) {
                _user.value = null
            } else {
                _error.value = result.exceptionOrNull()?.message ?: "Logout failed"
            }
        }
    }

    fun updatePassword(userId: String, newPassword: String) {
        viewModelScope.launch {
            _updatePasswordResult.value = null
            val result = userRepository.updateUser(userId, null, newPassword)
            _updatePasswordResult.value = result.map { Unit }
        }
    }
}
