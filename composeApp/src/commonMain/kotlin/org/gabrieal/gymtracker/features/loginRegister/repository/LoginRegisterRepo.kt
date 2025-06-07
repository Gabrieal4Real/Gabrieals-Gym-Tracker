package org.gabrieal.gymtracker.features.loginRegister.repository

import org.gabrieal.gymtracker.data.model.AuthResponse
import org.gabrieal.gymtracker.data.model.Profile

interface LoginRegisterRepo {
    suspend fun registerUser(email: String, password: String): AuthResponse
    suspend fun loginUser(email: String, password: String): AuthResponse
    suspend fun saveUser(uid: String, idToken: String, profile: Profile): Boolean
    suspend fun fetchUser(uid: String, idToken: String): Profile
}