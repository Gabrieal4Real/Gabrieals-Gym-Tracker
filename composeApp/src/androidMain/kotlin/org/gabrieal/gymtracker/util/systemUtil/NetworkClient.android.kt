package org.gabrieal.gymtracker.util.systemUtil

import io.ktor.client.engine.okhttp.OkHttp

actual fun getPlatformEngine() = OkHttp.create()