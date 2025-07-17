package org.gabrieal.gymtracker.features.loginRegister.viewmodel

import org.gabrieal.gymtracker.data.model.Profile

data class LoginRegisterUiState(
    val profile: Profile = Profile(),
    val isRegisterMode: Boolean = false,
    val userName: String? = null,
    val email: String? = null,
    val password: String? = null,
)
