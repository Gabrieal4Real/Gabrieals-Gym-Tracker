package org.gabrieal.gymtracker.util.systemUtil

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object SpotifyRedirectHandler {
    private val _codeFlow = MutableSharedFlow<String>(replay = 1)
    val codeFlow = _codeFlow.asSharedFlow()

    fun emitCode(code: String) {
        _codeFlow.tryEmit(code)
    }
}