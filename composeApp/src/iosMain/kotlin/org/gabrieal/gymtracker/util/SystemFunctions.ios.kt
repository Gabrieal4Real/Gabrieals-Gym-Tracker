package org.gabrieal.gymtracker.util

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS doesn't have a direct equivalent to Android's back button
    // This is a no-op implementation as iOS uses swipe gestures or navigation buttons
    // that are handled by the OS
}