package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val idToken: String,
    val localId: String
)
