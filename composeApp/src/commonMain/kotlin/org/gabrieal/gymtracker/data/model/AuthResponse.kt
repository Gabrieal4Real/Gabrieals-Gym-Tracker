package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val idToken: String,
    val email: String,
    val refreshToken: String,
    val expiresIn: String,
    val localId: String
)
