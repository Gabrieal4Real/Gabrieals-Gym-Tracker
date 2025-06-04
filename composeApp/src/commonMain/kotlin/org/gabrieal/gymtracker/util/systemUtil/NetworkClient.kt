package org.gabrieal.gymtracker.util.systemUtil

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


expect fun getPlatformEngine(): HttpClientEngine

val httpClient = HttpClient(getPlatformEngine()) {
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
}