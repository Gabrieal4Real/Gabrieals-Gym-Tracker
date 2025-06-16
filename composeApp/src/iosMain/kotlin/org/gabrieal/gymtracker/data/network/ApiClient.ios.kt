package org.gabrieal.gymtracker.data.network
import io.ktor.client.engine.darwin.Darwin

actual fun getPlatformEngine() = Darwin.create()