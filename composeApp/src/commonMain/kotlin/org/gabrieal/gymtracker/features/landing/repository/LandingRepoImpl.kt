package org.gabrieal.gymtracker.features.landing.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.gabrieal.gymtracker.data.model.SpotifyPlayerState
import org.gabrieal.gymtracker.data.network.SpotifyService

class LandingRepoImpl(private val spotifyService: SpotifyService) : LandingRepo {

    override suspend fun postPlayerState(
        accessToken: String,
        playerState: SpotifyPlayerState
    ): Flow<Any> {
        return spotifyService.postPlayerState(accessToken, playerState)
            .map { result ->
                runCatching { result.getOrThrow() }
                    .getOrElse { e ->
                        println("Error fetching player state: ${e.message}")
                        throw e
                    }
            }
    }
}