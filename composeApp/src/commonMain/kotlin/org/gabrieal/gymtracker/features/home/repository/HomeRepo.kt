package org.gabrieal.gymtracker.features.home.repository

import kotlinx.coroutines.flow.Flow
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.model.SpotifyTracks

interface HomeRepo {
    suspend fun requestSpotifyToken(): Flow<SpotifyRefreshTokenResponse>
    suspend fun getTrackInfo(spotifyUrls: List<String>, spotifyUid: String): Flow<SpotifyTracks>
}
