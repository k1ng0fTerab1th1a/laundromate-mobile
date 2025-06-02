package com.example.laundromate.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laundromate.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            authRepository.login(username, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, isLoggedIn = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = error.message)
                }
        }
    }

    fun register(username: String, password: String, email: String?) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            authRepository.register(username, password, email)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(isLoading = false, showRegisterSuccess = true)
                }
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(isLoading = false, error = error.message)
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    fun clearRegisterSuccess() {
        _uiState.value = _uiState.value.copy(showRegisterSuccess = false)
    }
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val showRegisterSuccess: Boolean = false
)