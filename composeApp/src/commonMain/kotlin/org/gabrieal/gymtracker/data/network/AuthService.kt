package org.gabrieal.gymtracker.data.network

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
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.gabrieal.gymtracker.data.model.AuthResponse
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.RegisterRequest

class AuthService(private val client: HttpClient) {

}
