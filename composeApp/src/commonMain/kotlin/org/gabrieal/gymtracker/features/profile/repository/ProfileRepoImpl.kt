package org.gabrieal.gymtracker.features.profile.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gabrieal.gymtracker.data.model.SpotifyProfile
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.network.SpotifyService

class ProfileRepoImpl(private val spotifyService: SpotifyService) : ProfileRepo {

    override suspend fun getExchangeToken(accessToken: String, codeVerifier: String): Flow<SpotifyRefreshTokenResponse> {
        return spotifyService.exchangeToken(accessToken, codeVerifier)
            .map { result ->
                runCatching { result.getOrThrow() }
                    .getOrElse { e ->
                        println("Error requesting Spotify token: ${e.message}")
                        throw e
                    }
            }
    }

    override suspend fun getSpotifyProfile(): Flow<SpotifyProfile> {
        return spotifyService.getSpotifyProfile()
            .map { result ->
                runCatching { result.getOrThrow() }
                    .getOrElse { e ->
                        println("Error requesting Spotify profile: ${e.message}")
                        throw e
                    }
            }
    }
}