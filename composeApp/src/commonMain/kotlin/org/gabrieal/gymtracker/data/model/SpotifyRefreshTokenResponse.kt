package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SpotifyRefreshTokenResponse(
    val access_token: String? = "",
    val expires_in: Int? = 0,
    val token_type: String? = "",
    val refresh_token: String? = ""
)