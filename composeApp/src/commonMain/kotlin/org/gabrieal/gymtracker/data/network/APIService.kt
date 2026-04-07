package org.gabrieal.gymtracker.data.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.Parameters
import io.ktor.http.encodeURLParameter
import io.ktor.http.formUrlEncode
import io.ktor.http.isSuccess
import org.gabrieal.gymtracker.util.systemUtil.SPOTIFY_CLIENT_ID

object APIService {
    internal const val spotifyClientId = SPOTIFY_CLIENT_ID

    internal const val spotifyRedirectUri = "gabriealgymtracker://callback"

    fun spotifyRefreshTokenPath(): String = "https://accounts.spotify.com/api/token"
    fun spotifyTrackPath(trackId: String): String = "https://api.spotify.com/v1/tracks?ids=$trackId"
    fun spotifyPlaybackPath(): String = "https://api.spotify.com/v1/me/player"
    fun spotifyRequestTokenUrl(): String = "https://accounts.spotify.com/api/token"

    fun spotifyUserProfilePath(): String = "https://api.spotify.com/v1/me"

    fun spotifyPlayPauseSkip(): String = "https://api.spotify.com/v1/me/player/"

    fun authUrl(codeChallenge: String): String =
        buildString {
            append("https://accounts.spotify.com/authorize?")
            append("client_id=$spotifyClientId")
            append("&response_type=code")
            append("&redirect_uri=${spotifyRedirectUri.encodeURLParameter()}")
            append("&code_challenge_method=S256")
            append("&code_challenge=$codeChallenge")
            append("&scope=")
            append(
                listOf(
                    "user-read-playback-state",
                    "user-modify-playback-state",
                    "user-read-currently-playing",
                    "user-read-private",
                    "user-read-email"
                ).joinToString("%20")
            )
        }

    fun refreshParams(refreshToken: String): String = Parameters.build {
        append("grant_type", "refresh_token")
        append("refresh_token", refreshToken)
        append("client_id", spotifyClientId)
    }.formUrlEncode()


    suspend inline fun <reified T> HttpResponse.handleResponse(): T {
        if (!status.isSuccess()) {
            val errorBody = bodyAsText()
            throw Exception("HTTP ${status.value}: ${status.description} - $errorBody")
        }
        return body()
    }
}