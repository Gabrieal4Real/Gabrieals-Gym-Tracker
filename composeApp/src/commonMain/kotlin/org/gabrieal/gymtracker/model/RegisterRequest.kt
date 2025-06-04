package org.gabrieal.gymtracker.model

import kotlinx.serialization.Serializable


@Serializable
data class RegisterRequest(
    val email: String,
    val password: String,
    val returnSecureToken: Boolean = true
)