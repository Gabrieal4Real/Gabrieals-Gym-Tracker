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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.longOrNull
import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.RefreshTokenResponse
import org.gabrieal.gymtracker.data.network.APIService.refreshTokenUrl
import org.gabrieal.gymtracker.data.sqldelight.getFirebaseInfoFromDB
import org.gabrieal.gymtracker.data.sqldelight.setFirebaseInfoToDB
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object APIService {
    private const val authBaseUrl = "https://identitytoolkit.googleapis.com/v1"
    private const val firestoreBaseUrl = "https://firestore.googleapis.com/v1"

    private const val apiKey = "[API_KEY]"
    private const val projectId = "[PROJECT_ID]"
    internal const val spotifyClientId = "[SPOTIFY_CLIENT_ID]"
    internal const val spotifyClientSecret = "[SPOTIFY_CLIENT_SECRET]"

    fun registerUrl(): String = "$authBaseUrl/accounts:signUp?key=$apiKey"
    fun loginUrl(): String = "$authBaseUrl/accounts:signInWithPassword?key=$apiKey"
    fun refreshTokenUrl(): String = "https://securetoken.googleapis.com/v1/token?key=$apiKey"
    fun userDocumentPath(uid: String): String =
        "$firestoreBaseUrl/projects/$projectId/databases/(default)/documents/users/$uid"
    fun spotifyTrackPath(trackId: String): String = "https://api.spotify.com/v1/tracks?ids=$trackId"
    fun spotifyRequestTokenUrl(): String = "https://accounts.spotify.com/api/token"
}

fun getSpotifyBody() =
    buildString {
        append("grant_type=client_credentials")
        append("&client_id=${APIService.spotifyClientId}")
        append("&client_secret=${APIService.spotifyClientSecret}")
    }

fun JsonElement.toFirestoreValue(): Map<String, Any>? = when (this) {
    is JsonPrimitive -> when {
        isString -> mapOf("stringValue" to content)
        booleanOrNull != null -> mapOf("booleanValue" to booleanOrNull!!)
        longOrNull != null -> mapOf("integerValue" to longOrNull!!)
        doubleOrNull != null -> mapOf("doubleValue" to doubleOrNull!!)
        else -> mapOf("stringValue" to content)
    }

    is JsonArray -> mapOf(
        "arrayValue" to mapOf("values" to mapNotNull { it.toFirestoreValue() })
    )

    is JsonObject -> mapOf(
        "mapValue" to mapOf("fields" to toFirestoreMap()["fields"]!!)
    )

    else -> null
}

fun JsonObject.toFirestoreMap(): Map<String, Any> {
    return mapOf(
        "fields" to mapValues { (_, value) ->
            value.toFirestoreValue() ?: mapOf("stringValue" to value.toString())
        }
    )
}

inline fun <reified T> T.toFirestoreDocument(): Map<String, Any> {
    val json = Json.encodeToJsonElement(this)
    require(json is JsonObject) {
        "Expected JsonObject but got ${json::class.simpleName}"
    }
    return json.toFirestoreMap()
}

inline fun <reified T> fromFirestoreDocument(document: JsonObject): T {
    val json = Json { ignoreUnknownKeys = true }

    val fields = document["fields"]?.jsonObjectOrNull()
        ?: error("Missing 'fields' object in Firestore document")

    val decoded = JsonObject(fields.mapValues { (_, v) ->
        v.jsonObject.values.first()
    })

    return json.decodeFromJsonElement(decoded)
}

fun JsonElement.jsonObjectOrNull(): JsonObject? = this as? JsonObject

suspend inline fun <reified T> HttpClient.makeRequest(
    method: HttpMethod,
    url: String,
    headers: Map<String, String> = emptyMap(),
    body: Any? = null,
    contentType: ContentType = ContentType.Application.Json
): Pair<Boolean, T> {
    val response: HttpResponse = this.request(url) {
        this.method = method
        contentType(contentType)

        headers.forEach { (key, value) ->
            header(key, value)
        }

        body?.let {
            setBody(it)
        }
    }

    return Pair(response.status.isSuccess(), response.body())
}

@OptIn(ExperimentalTime::class)
suspend inline fun <reified T : Any> HttpClient.makeAuthenticatedRequest(
    method: HttpMethod,
    url: String,
    headers: Map<String, String> = emptyMap(),
    body: Any? = null,
    refreshTokenIfNeeded: Boolean = true
): Pair<Boolean, T> {
    var result = makeRequest<T>(method, url, headers, body)

    if (result.first || !refreshTokenIfNeeded || !isUnauthorizedError(result.second)) return result

    val refreshToken = getFirebaseInfoFromDB().refreshToken.orEmpty()
    if (refreshToken.isEmpty()) return result

    val refreshResult = makeRequest<RefreshTokenResponse>(
        method = HttpMethod.Post,
        url = refreshTokenUrl(),
        body = mapOf("grant_type" to "refresh_token", "refresh_token" to refreshToken)
    )

    if (!refreshResult.first) return result

    val tokenResponse = refreshResult.second
    val newToken = tokenResponse.id_token

    val newFirebaseInfo = FirebaseInfo(
        uid = tokenResponse.user_id,
        token = newToken,
        refreshToken = tokenResponse.refresh_token,
        expiresAt = Clock.System.now()
            .toEpochMilliseconds() + (tokenResponse.expires_in.toLongOrNull() ?: 3600L) * 1000
    )
    setFirebaseInfoToDB(newFirebaseInfo)

    val updatedHeaders = headers.toMutableMap().apply {
        if (containsKey("Authorization")) {
            this["Authorization"] = "Bearer $newToken"
        }
    }

    result = makeRequest(method, url, updatedHeaders, body)

    return result
}

fun isUnauthorizedError(response: Any): Boolean {
    return (response as? Map<*, *>)?.get("error")?.let { error ->
        (error as? Map<*, *>)?.let {
            it["code"] == 401 || it["code"] == 401.0 || it["status"] == "UNAUTHENTICATED"
        }
    } == true
}