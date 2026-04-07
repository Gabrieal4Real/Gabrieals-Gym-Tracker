package org.gabrieal.gymtracker.util.systemUtil

import io.ktor.util.encodeBase64

object PKCE {
    fun generateCodeVerifier(): String =
        (1..64).map {
            (('A'..'Z') + ('a'..'z') + ('0'..'9')).random()
        }.joinToString("")

    fun generateCodeChallenge(verifier: String): String {
        val sha256 = platformSha256(verifier.encodeToByteArray())
        return sha256.encodeBase64UrlSafe()
    }
}

fun ByteArray.encodeBase64UrlSafe(): String {
    return encodeBase64()
        .replace("+", "-")
        .replace("/", "_")
        .replace("=", "")
}

expect fun platformSha256(input: ByteArray): ByteArray