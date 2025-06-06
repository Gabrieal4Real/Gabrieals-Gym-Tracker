package org.gabrieal.gymtracker.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FirebaseInfo(
    var uid: String? = null,
    var token: String? = null,
    var refreshToken: String? = null,
    var expiresAt: Long? = null
)