package org.gabrieal.gymtracker.features.home.repository

import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.model.SpotifyTracks

interface HomeRepo {
    suspend fun getTrackInfo(spotifyUrls: List<String>, spotifyUid: String): SpotifyTracks?

    suspend fun requestSpotifyToken(): SpotifyRefreshTokenResponse?
}