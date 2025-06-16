package org.gabrieal.gymtracker.data.network

import io.ktor.client.engine.okhttp.OkHttp

actual fun getPlatformEngine() = OkHttp.create()