package org.gabrieal.gymtracker.features.home.repository

import org.gabrieal.gymtracker.data.model.SpotifyRefreshTokenResponse
import org.gabrieal.gymtracker.data.model.SpotifyTracks
import org.gabrieal.gymtracker.data.network.SpotifyService

class HomeRepoImpl(private val spotifyService: SpotifyService) : HomeRepo {
    private fun extractTrackId(spotifyUrl: String): String? {
        val regex = Regex("open\\.spotify\\.com/track/([a-zA-Z0-9]+)")
        return regex.find(spotifyUrl)?.groupValues?.get(1)
    }

    override suspend fun requestSpotifyToken(): SpotifyRefreshTokenResponse? {
        val (success, result) = spotifyService.requestSpotifyToken()
        return if (success) result else null
    }

    override suspend fun getTrackInfo(
        spotifyUrls: List<String>,
        spotifyUid: String
    ): SpotifyTracks? {
        val listOfTrackIds = mutableListOf<String>()
        spotifyUrls.forEach {
            extractTrackId(it)?.let { it1 -> listOfTrackIds.add(it1) }
        }

        val (success, result) = spotifyService.getTracks(listOfTrackIds, spotifyUid)
        return if (success) result as? SpotifyTracks else null
    }
}