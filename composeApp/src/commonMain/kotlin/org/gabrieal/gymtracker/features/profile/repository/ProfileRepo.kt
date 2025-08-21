package org.gabrieal.gymtracker.features.profile.repository

import kotlinx.coroutines.flow.Flow
import org.gabrieal.gymtracker.data.model.SpotifyProfile
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse

interface ProfileRepo {
    suspend fun getExchangeToken(
        accessToken: String,
        codeVerifier: String
    ): Flow<SpotifyRefreshTokenResponse>

    suspend fun getSpotifyProfile(accessToken: String): Flow<SpotifyProfile>
}
