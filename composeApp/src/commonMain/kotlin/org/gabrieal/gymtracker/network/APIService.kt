package org.gabrieal.gymtracker.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.contentType
import io.ktor.http.isSuccess

object APIService {
    private val authBaseUrl = "https://identitytoolkit.googleapis.com/v1"
    private val firestoreBaseUrl = "https://firestore.googleapis.com/v1"
    private val apiKey = "AIzaSyCepDau1NuwTqpzdKeTBm-klT48vbJDpZ8"
    private val projectId = "gabrieal-gym-tracker"

    fun registerUrl(): String = "$authBaseUrl/accounts:signUp?key=$apiKey"

    fun userDocumentPath(uid: String): String = "$firestoreBaseUrl/projects/$projectId/databases/(default)/documents/users/$uid"
}

suspend inline fun <reified T> HttpClient.makeRequest(
    method: HttpMethod,
    url: String,
    headers: Map<String, String> = emptyMap(),
    body: Any? = null
): Pair<Boolean, T> {
    val response: HttpResponse = this.request(url) {
        this.method = method
        contentType(ContentType.Application.Json)

        headers.forEach { (key, value) ->
            header(key, value)
        }

        body?.let {
            setBody(it)
        }
    }

    if (!response.status.isSuccess()) {
        throw Exception("Request failed: ${response.status}")
    }

    return Pair(response.status.isSuccess(), response.body())
}