package org.gabrieal.gymtracker.features.loginRegister.repository

import org.gabrieal.gymtracker.network.FirebaseService

class LoginRegisterRepoImpl(private val firebaseService: FirebaseService): LoginRegisterRepo {
    override suspend fun registerAndSave(
        email: String,
        password: String,
        data: Map<String, Any>
    ): Boolean {
        val authResponse = firebaseService.registerWithEmail(email, password)
        return firebaseService.saveUserData(authResponse.second.localId, authResponse.second.idToken, data).first
    }
}