package org.gabrieal.gymtracker.data.network

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.gabrieal.gymtracker.data.model.SpotifyPlayback
import org.gabrieal.gymtracker.data.model.SpotifyProfile
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.model.SpotifyTracks

class SpotifyService(private val client: HttpClient) {

    fun exchangeToken(code: String, codeVerifier: String): Flow<Result<SpotifyRefreshTokenResponse>> = flow {
        emit(
            runCatching {
                val response: HttpResponse = client.submitForm(
                    url = APIService.spotifyRequestTokenUrl(),
                    formParameters = Parameters.build {
                        append("grant_type", "authorization_code")
                        append("code", code)
                        append("redirect_uri", APIService.spotifyRedirectUri)
                        append("client_id", APIService.spotifyClientId)
                        append("code_verifier", codeVerifier)
                    }
                )
                response.body()
            }
        )
    }

    fun getTracks(trackIds: List<String>, accessToken: String): Flow<Result<SpotifyTracks>> = flow {
        emit(
            runCatching {
                val response: HttpResponse = client.request(
                    APIService.spotifyTrackPath(trackIds.joinToString("%2C"))
                ) {
                    method = HttpMethod.Get
                    header("Authorization", "Bearer $accessToken")
                }
                response.body<SpotifyTracks>()
            }
        )
    }

    fun getSpotifyPlayback(accessToken: String): Flow<Result<SpotifyPlayback>> = flow {
        emit(
            runCatching {
                val response: HttpResponse = client.request(APIService.spotifyPlaybackPath()) {
                    method = HttpMethod.Get
                    header("Authorization", "Bearer $accessToken")
                }

                if (!response.status.isSuccess()) {
                    val errorBody = response.bodyAsText()
                    throw Exception("HTTP ${response.status.value}: ${response.status.description} - $errorBody")
                }

                response.body<SpotifyPlayback>()
            }
        )
    }

    fun getSpotifyProfile(accessToken: String): Flow<Result<SpotifyProfile>> = flow {
        emit(
            runCatching {
                val response: HttpResponse = client.request(APIService.spotifyUserProfilePath()) {
                    method = HttpMethod.Get
                    header("Authorization", "Bearer $accessToken")
                }

                if (!response.status.isSuccess()) {
                    val errorBody = response.bodyAsText()
                    throw Exception("HTTP ${response.status.value}: ${response.status.description} - $errorBody")
                }

                response.body<SpotifyProfile>()
            }
        )
    }
}