package org.gabrieal.gymtracker.features.loginRegister.repository

import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.network.AuthService

class LoginRegisterRepoImpl(private val authService: AuthService) : LoginRegisterRepo {
    override suspend fun registerUser(email: String, password: String) =
        authService.registerWithEmail(email, password).second

    override suspend fun loginUser(email: String, password: String) =
        authService.loginWithEmail(email, password).second

    override suspend fun saveUser(uid: String, idToken: String, profile: Profile) =
        authService.saveUserData(uid, idToken, profile).first

    override suspend fun fetchUser(uid: String, idToken: String) =
        authService.getUserData(uid, idToken).second
}