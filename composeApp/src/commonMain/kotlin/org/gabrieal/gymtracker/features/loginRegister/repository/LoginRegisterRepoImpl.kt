package org.gabrieal.gymtracker.features.loginRegister.repository

import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.network.FirebaseService

class LoginRegisterRepoImpl(private val firebaseService: FirebaseService): LoginRegisterRepo {
    override suspend fun registerAndSave(email: String, password: String, profile: Profile): Boolean {
        val authResponse = firebaseService.registerWithEmail(email, password)
        return firebaseService.saveUserData(authResponse.second.localId, authResponse.second.idToken, profile).first
    }

    override suspend fun loginAndSave(email: String, password: String, profile: Profile): Boolean {
        val authResponse = firebaseService.loginWithEmail(email, password)
        return firebaseService.saveUserData(authResponse.second.localId, authResponse.second.idToken, profile).first
    }
}