package org.gabrieal.gymtracker.data.network

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.single
import org.gabrieal.gymtracker.data.model.SpotifyPlayback
import org.gabrieal.gymtracker.data.model.SpotifyPlayerState
import org.gabrieal.gymtracker.data.model.SpotifyProfile
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.model.SpotifyTracks
import org.gabrieal.gymtracker.data.network.APIService.handleResponse
import org.gabrieal.gymtracker.data.sqldelight.getSpotifyTokenFromDB
import org.gabrieal.gymtracker.data.sqldelight.updateSpotifyTokenToDB

class SpotifyService(private val client: HttpClient) {

    private suspend inline fun <reified T> withTokenRefresh(
        crossinline block: suspend (String) -> HttpResponse
    ): T {
        val token = getSpotifyTokenFromDB() ?: throw Exception("No token found")

        var response = block(token.access_token ?: throw Exception("No access token"))

        if (response.status.value == 401) {
            val refreshed = refreshSpotifyToken(
                token.refresh_token ?: throw Exception("No refresh token")
            ).single().getOrThrow()

            updateSpotifyTokenToDB(refreshed)
            val newAccess = refreshed.access_token ?: throw Exception("Refresh failed")
            response = block(newAccess)
        }

        return response.handleResponse()
    }

    private fun <T> wrapFlow(block: suspend () -> T): Flow<Result<T>> = flow {
        emit(runCatching { block() })
    }

    fun refreshSpotifyToken(refreshToken: String): Flow<Result<SpotifyRefreshTokenResponse>> =
        wrapFlow {
            if (refreshToken.isBlank()) throw IllegalArgumentException("Empty refresh token")
            client.request(APIService.spotifyRefreshTokenPath()) {
                method = HttpMethod.Post
                contentType(ContentType.Application.FormUrlEncoded)
                setBody(APIService.refreshParams(refreshToken))
            }.handleResponse()
        }

    fun exchangeToken(
        code: String,
        codeVerifier: String
    ): Flow<Result<SpotifyRefreshTokenResponse>> =
        wrapFlow {
            client.submitForm(
                url = APIService.spotifyRequestTokenUrl(),
                formParameters = Parameters.build {
                    append("grant_type", "authorization_code")
                    append("code", code)
                    append("redirect_uri", APIService.spotifyRedirectUri)
                    append("client_id", APIService.spotifyClientId)
                    append("code_verifier", codeVerifier)
                }
            ).handleResponse()
        }

    fun getTracks(trackIds: List<String>): Flow<Result<SpotifyTracks>> =
        wrapFlow {
            withTokenRefresh { token ->
                client.request(APIService.spotifyTrackPath(trackIds.joinToString("%2C"))) {
                    method = HttpMethod.Get
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

    fun getSpotifyPlayback(): Flow<Result<SpotifyPlayback>> =
        wrapFlow {
            withTokenRefresh { token ->
                client.request(APIService.spotifyPlaybackPath()) {
                    method = HttpMethod.Get
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

    fun getSpotifyProfile(): Flow<Result<SpotifyProfile>> =
        wrapFlow {
            withTokenRefresh { token ->
                client.request(APIService.spotifyUserProfilePath()) {
                    method = HttpMethod.Get
                    header(HttpHeaders.Authorization, "Bearer $token")
                }
            }
        }

    fun postPlayerState(playerState: SpotifyPlayerState, deviceId: String?): Flow<Result<Any>> =
        wrapFlow {
            withTokenRefresh { token ->
                client.request(APIService.spotifyPlayPauseSkip() + playerState.playerState) {
                    method = if (playerState.seekable) HttpMethod.Post else HttpMethod.Put
                    header(HttpHeaders.Authorization, "Bearer $token")
                    url.parameters.append("device_id", deviceId ?: "")
                }
            }
        }
}