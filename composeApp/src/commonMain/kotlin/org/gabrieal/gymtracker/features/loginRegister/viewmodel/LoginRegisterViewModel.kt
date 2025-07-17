package org.gabrieal.gymtracker.features.loginRegister.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.Profile

class LoginRegisterViewModel() {

    private val _uiState = MutableStateFlow(LoginRegisterUiState())
    val uiState: StateFlow<LoginRegisterUiState> = _uiState.asStateFlow()

    private var callback: ((Profile?) -> Unit)? = null

    fun setProfile(profile: Profile) = _uiState.update { it.copy(profile = profile) }

    fun setCallback(callback: (Profile?) -> Unit) {
        this.callback = callback
    }

    fun changeRegisterMode() = _uiState.update { it.copy(isRegisterMode = !it.isRegisterMode) }

    private fun clearUserInput() =
        _uiState.update { it.copy(userName = "", email = "", password = "") }


    private fun updateProfile(email: String? = null, userName: String? = null) {
        val currentProfile = _uiState.value.profile
        val updatedProfile = currentProfile.copy(
            email = email ?: currentProfile.email,
            userName = userName ?: currentProfile.userName
        )
        _uiState.update { it.copy(profile = updatedProfile) }
    }

    fun updateName(userName: String) = _uiState.update { it.copy(userName = userName) }

    fun updateEmail(email: String) = _uiState.update { it.copy(email = email) }

    fun updatePassword(password: String) = _uiState.update { it.copy(password = password) }
}