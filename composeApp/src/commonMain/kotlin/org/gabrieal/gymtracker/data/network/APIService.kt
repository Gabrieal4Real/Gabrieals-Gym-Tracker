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
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.long
import kotlinx.serialization.json.longOrNull

object APIService {
    private val authBaseUrl = "https://identitytoolkit.googleapis.com/v1"
    private val firestoreBaseUrl = "https://firestore.googleapis.com/v1"
    private val apiKey = "AIzaSyCepDau1NuwTqpzdKeTBm-klT48vbJDpZ8"
    private val projectId = "gabrieal-gym-tracker"

    fun registerUrl(): String = "$authBaseUrl/accounts:signUp?key=$apiKey"
    fun loginUrl(): String = "$authBaseUrl/accounts:signInWithPassword?key=$apiKey"
    fun userDocumentPath(uid: String): String = "$firestoreBaseUrl/projects/$projectId/databases/(default)/documents/users/$uid"
}

fun JsonObject.toFirestoreMap(): Map<String, Any> {
    return mapOf(
        "fields" to this.mapValues { (_, value) ->
            when (value) {
                is JsonPrimitive -> when {
                    value.isString -> mapOf("stringValue" to value.toString())
                    value.booleanOrNull != null -> mapOf("booleanValue" to value.boolean.toString())
                    value.longOrNull != null -> mapOf("integerValue" to value.long.toString())
                    value.doubleOrNull != null -> mapOf("doubleValue" to value.double.toString())
                    else -> mapOf("stringValue" to value.toString())
                }

                is JsonArray -> mapOf(
                    "arrayValue" to mapOf(
                        "values" to value.mapNotNull { element ->
                            when (element) {
                                is JsonPrimitive -> when {
                                    element.isString -> mapOf("stringValue" to element.toString())
                                    element.booleanOrNull != null -> mapOf("booleanValue" to element.boolean.toString())
                                    element.longOrNull != null -> mapOf("integerValue" to element.long.toString())
                                    element.doubleOrNull != null -> mapOf("doubleValue" to element.double.toString())
                                    else -> null
                                }

                                is JsonObject -> mapOf("mapValue" to mapOf("fields" to element.toFirestoreMap()["fields"]!!))
                                else -> null
                            }
                        }
                    )
                )

                is JsonObject -> mapOf(
                    "mapValue" to mapOf("fields" to value.toFirestoreMap()["fields"]!!)
                )

                else -> mapOf("stringValue" to value.toString())
            }
        }
    )
}

inline fun <reified T> T.toFirestoreDocument(): Map<String, Any> {
    val json = Json.encodeToJsonElement(this)

    if (json !is JsonObject) {
        println("Expected JsonObject but got ${json::class.simpleName}")
        throw IllegalArgumentException("Expected JsonObject but got ${json::class.simpleName}")
    }

    return json.toFirestoreMap()
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

    return Pair(response.status.isSuccess(), response.body())
}