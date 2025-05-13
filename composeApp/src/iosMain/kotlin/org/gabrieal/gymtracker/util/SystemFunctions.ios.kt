package org.gabrieal.gymtracker.util

import androidx.compose.runtime.Composable
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    // iOS doesn't have a direct equivalent to Android's back button
    // This is a no-op implementation as iOS uses swipe gestures or navigation buttons
    // that are handled by the OS
}

@Composable
actual fun openURL(url: String) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null && UIApplication.sharedApplication.canOpenURL(nsUrl)) {
        UIApplication.sharedApplication.openURL(
            nsUrl,
            options = emptyMap<Any?, Any>(),
            completionHandler = { success ->
                println("URL opened successfully: $success")
            }
        )
    }
}