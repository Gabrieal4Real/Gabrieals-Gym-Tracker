package org.gabrieal.gymtracker.features.loginRegister.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo

class LoginRegisterViewModel(private val loginRegisterRepo: LoginRegisterRepo) {
    private val _uiState = MutableStateFlow(LoginRegisterUiState())
    val uiState: StateFlow<LoginRegisterUiState> = _uiState.asStateFlow()

    suspend fun registerNewUser(email: String, password: String) {
        try {
            val isSuccess = loginRegisterRepo.registerAndSave(email, password, mapOf("email" to email, "name" to email))
            _uiState.update { it.copy(isSuccess = isSuccess) }
        } catch (e: Exception) {
            _uiState.update { it.copy(error = e.message) }
        }
    }
}