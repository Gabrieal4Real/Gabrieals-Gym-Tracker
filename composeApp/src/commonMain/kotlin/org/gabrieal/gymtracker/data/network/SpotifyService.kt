package org.gabrieal.gymtracker.data.network

import io.ktor.client.HttpClient
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse

class SpotifyService(private val client: HttpClient) {
    suspend fun requestSpotifyToken(): Pair<Boolean, SpotifyRefreshTokenResponse> {
        return try {
            val refreshResult = client.makeRequest<SpotifyRefreshTokenResponse>(
                method = HttpMethod.Post,
                url = APIService.spotifyRequestTokenUrl(),
                contentType = ContentType.Application.FormUrlEncoded,
                headers = mapOf("Content-Type" to "application/x-www-form-urlencoded"),
                body = getSpotifyBody()
            )

            refreshResult
        } catch (e: Exception) {
            println("Failed to requestSpotifyToken: ${e.message}")
            Pair(false, SpotifyRefreshTokenResponse())
        }
    }

    suspend fun getTracks(trackIds: List<String>, spotifyUid: String): Pair<Boolean, Any> {
        return try {
            client.makeRequest(
                method = HttpMethod.Get,
                url = APIService.spotifyTrackPath(trackIds.joinToString(",")),
                headers = mapOf("Authorization" to "Bearer $spotifyUid")
            )
        } catch (e: Exception) {
            println("Failed to getTrack: ${e.message}")
            Pair(false, e.message.orEmpty())
        }
    }
}