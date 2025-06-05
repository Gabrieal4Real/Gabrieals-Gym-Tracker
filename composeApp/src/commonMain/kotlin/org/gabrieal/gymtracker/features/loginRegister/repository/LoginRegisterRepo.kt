package org.gabrieal.gymtracker.features.loginRegister.repository

import org.gabrieal.gymtracker.data.model.Profile

interface LoginRegisterRepo {
    suspend fun registerAndSave(email: String, password: String, profile: Profile): Boolean
    suspend fun loginAndSave(email: String, password: String, profile: Profile): Boolean
}