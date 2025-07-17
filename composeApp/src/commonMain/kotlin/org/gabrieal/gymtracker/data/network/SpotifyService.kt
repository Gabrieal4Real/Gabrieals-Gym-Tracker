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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.model.SpotifyTracks

class SpotifyService(private val client: HttpClient) {

    fun requestSpotifyToken(): Flow<Result<SpotifyRefreshTokenResponse>> = flow {
        emit(
            runCatching {
                val response: HttpResponse = client.request(APIService.spotifyRequestTokenUrl()) {
                    method = HttpMethod.Post
                    header("Content-Type", "application/x-www-form-urlencoded")
                    contentType(ContentType.Application.FormUrlEncoded)
                    setBody(getSpotifyBody())
                }
                response.body<SpotifyRefreshTokenResponse>()
            }
        )
    }

    fun getTracks(trackIds: List<String>, spotifyUid: String): Flow<Result<SpotifyTracks>> = flow {
        emit(
            runCatching {
                val response: HttpResponse = client.request(
                    APIService.spotifyTrackPath(trackIds.joinToString("%2C"))
                ) {
                    method = HttpMethod.Get
                    header("Authorization", "Bearer $spotifyUid")
                }
                response.body<SpotifyTracks>()
            }
        )
    }
}