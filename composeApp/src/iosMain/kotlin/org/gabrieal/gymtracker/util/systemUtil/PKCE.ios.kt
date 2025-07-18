package org.gabrieal.gymtracker.util.systemUtil

import platform.CommonCrypto.*
import kotlinx.cinterop.*

actual fun platformSha256(input: ByteArray): ByteArray = memScoped {
    val digest = ByteArray(CC_SHA256_DIGEST_LENGTH)
    input.usePinned { pinnedInput ->
        digest.usePinned { pinnedDigest ->
            CC_SHA256(pinnedInput.addressOf(0), input.size.convert(), pinnedDigest.addressOf(0))
        }
    }
    digest
}