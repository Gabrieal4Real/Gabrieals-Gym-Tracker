package org.gabrieal.gymtracker.features.home.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gabrieal.gymtracker.data.model.SpotifyPlayback
import org.gabrieal.gymtracker.data.model.SpotifyTracks
import org.gabrieal.gymtracker.data.network.SpotifyService

class HomeRepoImpl(private val spotifyService: SpotifyService) : HomeRepo {
    private fun extractTrackId(spotifyUrl: String): String? {
        val regex = Regex("open\\.spotify\\.com/track/([a-zA-Z0-9]+)")
        return regex.find(spotifyUrl)?.groupValues?.get(1)
    }

    override suspend fun getTrackInfo(spotifyUrls: List<String>): Flow<SpotifyTracks> {
        val trackIds = spotifyUrls.mapNotNull { extractTrackId(it) }

        return spotifyService.getTracks(trackIds)
            .map { result ->
                runCatching { result.getOrThrow() }
                    .getOrElse { e ->
                        println("Error fetching track info: ${e.message}")
                        throw e
                    }
            }
    }

    override suspend fun getCurrentPlayback(): Flow<SpotifyPlayback> {
        return spotifyService.getSpotifyPlayback()
            .map { result ->
                runCatching { result.getOrThrow() }
                    .getOrElse { e ->
                        println("Error fetching current playback info: ${e.message}")
                        throw e
                    }
            }
    }
}