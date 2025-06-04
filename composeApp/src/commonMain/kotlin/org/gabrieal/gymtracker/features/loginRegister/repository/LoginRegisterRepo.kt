package org.gabrieal.gymtracker.features.loginRegister.repository

interface LoginRegisterRepo {
    suspend fun registerAndSave(email: String, password: String, data: Map<String, Any>): Boolean
}