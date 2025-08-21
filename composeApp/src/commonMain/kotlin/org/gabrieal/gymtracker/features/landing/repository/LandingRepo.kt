package org.gabrieal.gymtracker.features.landing.repository

import kotlinx.coroutines.flow.Flow
import org.gabrieal.gymtracker.data.model.SpotifyPlayerState

interface LandingRepo {
    suspend fun postPlayerState(accessToken: String, playerState: SpotifyPlayerState): Flow<Any>
}
