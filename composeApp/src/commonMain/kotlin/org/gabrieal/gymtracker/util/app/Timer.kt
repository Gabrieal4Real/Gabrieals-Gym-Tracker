package org.gabrieal.gymtracker.util.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
@Composable
fun ElapsedTimeDisplay(startTime: Instant?): String {
    if (startTime == null) return ""

    var now by remember { mutableStateOf(Clock.System.now()) }
    LaunchedEffect(startTime) {
        while (true) {
            now = Clock.System.now()
            delay(1000)
        }
    }
    val elapsed = now - startTime

    return formatRestTime(elapsed.inWholeSeconds.toInt())
}