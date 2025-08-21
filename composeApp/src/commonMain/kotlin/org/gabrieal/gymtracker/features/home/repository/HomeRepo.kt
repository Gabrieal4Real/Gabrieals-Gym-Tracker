package org.gabrieal.gymtracker.features.home.repository

import kotlinx.coroutines.flow.Flow
import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.model.SpotifyTracks

interface HomeRepo {
    suspend fun getTrackInfo(spotifyUrls: List<String>, accessToken: String): Flow<SpotifyTracks>
    suspend fun getCurrentPlayback(accessToken: String): Flow<Any>
}
