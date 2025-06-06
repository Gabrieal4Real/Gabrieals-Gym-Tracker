package org.gabrieal.gymtracker.features.loginRegister.repository

import org.gabrieal.gymtracker.data.model.AuthResponse
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.network.FirebaseService

class LoginRegisterRepoImpl(private val firebaseService: FirebaseService): LoginRegisterRepo {
    override suspend fun registerUser(email: String, password: String): AuthResponse {
        return firebaseService.registerWithEmail(email, password).second
    }

    override suspend fun loginUser(email: String, password: String): AuthResponse {
        return firebaseService.loginWithEmail(email, password).second
    }

    override suspend fun saveUser(uid: String, idToken: String, profile: Profile): Boolean {
        return firebaseService.saveUserData(uid, idToken, profile).first
    }
}