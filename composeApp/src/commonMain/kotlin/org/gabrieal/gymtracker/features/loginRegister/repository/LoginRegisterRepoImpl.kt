package org.gabrieal.gymtracker.features.loginRegister.repository

import org.gabrieal.gymtracker.data.model.AuthResponse
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.network.AuthService

class LoginRegisterRepoImpl(private val authService: AuthService): LoginRegisterRepo {
    override suspend fun registerUser(email: String, password: String): AuthResponse {
        return authService.registerWithEmail(email, password).second
    }

    override suspend fun loginUser(email: String, password: String): AuthResponse {
        return authService.loginWithEmail(email, password).second
    }

    override suspend fun saveUser(uid: String, idToken: String, profile: Profile): Boolean {
        return authService.saveUserData(uid, idToken, profile).first
    }
}