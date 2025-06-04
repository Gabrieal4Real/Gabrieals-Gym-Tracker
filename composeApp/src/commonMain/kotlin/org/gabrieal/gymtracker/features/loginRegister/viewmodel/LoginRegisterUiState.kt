package org.gabrieal.gymtracker.features.loginRegister.viewmodel

import org.gabrieal.gymtracker.model.AuthResponse

data class LoginRegisterUiState(
    val isSuccess: Boolean? = false,
    val error: String? = null
)
