package org.gabrieal.gymtracker.data.network

import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import org.gabrieal.gymtracker.data.model.AuthResponse
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.RegisterRequest

class FirebaseService(private val client: HttpClient) {
    suspend fun loginWithEmail(email: String, password: String): Pair<Boolean, AuthResponse> {
        return client.makeRequest(
            method = HttpMethod.Post,
            url = APIService.loginUrl(),
            body = RegisterRequest(email, password, true)
        )
    }

    suspend fun registerWithEmail(email: String, password: String): Pair<Boolean, AuthResponse> {
        return client.makeRequest(
            method = HttpMethod.Post,
            url = APIService.registerUrl(),
            body = RegisterRequest(email, password, true)
        )
    }

    suspend fun saveUserData(uid: String, idToken: String, profile: Profile): Pair<Boolean, Any> {
        try {
            return client.makeRequest(
                method = HttpMethod.Patch,
                headers = mapOf("Authorization" to "Bearer $idToken"),
                url = APIService.userDocumentPath(uid),
                body = profile.toFirestoreDocument()
            )
        } catch (e: Exception) {
            println("Failed to save user data: ${e.message}")
            return Pair(false, "Failed to save user data: ${e.message}")
        }
    }
}