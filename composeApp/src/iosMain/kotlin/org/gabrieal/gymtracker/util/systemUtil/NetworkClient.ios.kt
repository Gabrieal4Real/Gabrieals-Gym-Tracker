package org.gabrieal.gymtracker.util.systemUtil
import io.ktor.client.engine.darwin.Darwin

actual fun getPlatformEngine() = Darwin.create()