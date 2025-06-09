package org.gabrieal.gymtracker.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gabrieal.gymtracker.data.model.AuthResponse
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.RegisterRequest

class AuthService(private val client: HttpClient) {
    suspend fun loginWithEmail(email: String, password: String): Pair<Boolean, AuthResponse> {
        return authRequest(APIService.loginUrl(), email, password)
    }

    suspend fun registerWithEmail(email: String, password: String): Pair<Boolean, AuthResponse> {
        return authRequest(APIService.registerUrl(), email, password)
    }

    private suspend fun authRequest(
        url: String,
        email: String,
        password: String
    ): Pair<Boolean, AuthResponse> {
        return try {
            val response: HttpResponse = client.request(url) {
                method = HttpMethod.Post
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(email, password, true))
            }

            if (response.status.isSuccess()) {
                Pair(true, response.body())
            } else {
                throw Exception(extractErrorMessage(response))
            }
        } catch (e: Exception) {
            throw Exception(e.message ?: "Unknown error during authentication")
        }
    }

    private suspend fun extractErrorMessage(response: HttpResponse): String {
        return try {
            val errorBody = response.body<String>()
            val json = Json.decodeFromString<JsonObject>(errorBody)
            when (val code = json["error"]?.jsonObject?.get("message")?.jsonPrimitive?.content) {
                "EMAIL_EXISTS" -> "This email is already registered. Please try logging in instead."
                "EMAIL_NOT_FOUND" -> "There is no user record corresponding to this email. Please register first."
                else -> code ?: "The email address or password is not valid."
            }
        } catch (e: Exception) {
            "Error processing response: ${e.message}"
        }
    }

    suspend fun saveUserData(uid: String, idToken: String, profile: Profile): Pair<Boolean, Any> {
        return try {
            client.makeAuthenticatedRequest(
                method = HttpMethod.Patch,
                url = APIService.userDocumentPath(uid),
                headers = mapOf("Authorization" to "Bearer $idToken"),
                body = profile.toFirestoreDocument()
            )
        } catch (e: Exception) {
            println("Failed to saveUserData: ${e.message}")
            Pair(false, e.message.orEmpty())
        }
    }

    suspend fun getUserData(uid: String, idToken: String): Pair<Boolean, Profile> {
        return try {
            val (success, jsonElement) = client.makeAuthenticatedRequest<JsonObject>(
                method = HttpMethod.Get,
                url = APIService.userDocumentPath(uid),
                headers = mapOf("Authorization" to "Bearer $idToken")
            )

            if (!success) return Pair(false, Profile())

            val user = fromFirestoreDocument<Profile>(jsonElement)
            Pair(true, user)

        } catch (e: Exception) {
            println("Failed to getUserData: ${e.message}")
            Pair(false, Profile())
        }
    }
}