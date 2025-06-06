package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class RefreshTokenResponse(
    val access_token: String = "",
    val expires_in: String = "",
    val token_type: String = "",
    val refresh_token: String = "",
    val id_token: String = "",
    val user_id: String = "",
    val project_id: String = ""
)