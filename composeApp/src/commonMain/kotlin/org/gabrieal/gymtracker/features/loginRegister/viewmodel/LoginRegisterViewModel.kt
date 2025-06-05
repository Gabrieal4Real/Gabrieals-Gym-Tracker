package org.gabrieal.gymtracker.features.loginRegister.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo

class LoginRegisterViewModel(private val loginRegisterRepo: LoginRegisterRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(LoginRegisterUiState())
    val uiState: StateFlow<LoginRegisterUiState> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun registerNewUser(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val isSuccess = loginRegisterRepo.registerAndSave(email, password, mapOf("email" to email, "name" to email))
                _uiState.update { it.copy(isSuccess = isSuccess) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clear() {
        viewModelScope.cancel()
    }
}