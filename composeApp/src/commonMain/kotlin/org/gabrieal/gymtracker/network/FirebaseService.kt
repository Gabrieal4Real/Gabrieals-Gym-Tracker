package org.gabrieal.gymtracker.network

import io.ktor.client.HttpClient
import io.ktor.http.HttpMethod
import org.gabrieal.gymtracker.model.AuthResponse
import org.gabrieal.gymtracker.model.RegisterRequest

class FirebaseService(private val client: HttpClient) {
    suspend fun registerWithEmail(email: String, password: String): Pair<Boolean, AuthResponse> {
        return client.makeRequest(
            method = HttpMethod.Post,
            url = APIService.registerUrl(),
            body = RegisterRequest(email, password)
        )
    }

    suspend fun saveUserData(uid: String, idToken: String, data: Map<String, Any>): Pair<Boolean, Any> {
        val document =
            mapOf("fields" to data.mapValues { mapOf("stringValue" to it.value.toString()) })
        return client.makeRequest(
            method = HttpMethod.Patch,
            headers = mapOf("Authorization" to "Bearer $idToken"),
            url = APIService.userDocumentPath(uid),
            body = document
        )
    }
}