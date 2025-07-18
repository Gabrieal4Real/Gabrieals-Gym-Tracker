package org.gabrieal.gymtracker.util.systemUtil

import java.security.MessageDigest

actual fun platformSha256(input: ByteArray): ByteArray {
    val digest = MessageDigest.getInstance("SHA-256")
    return digest.digest(input)
}