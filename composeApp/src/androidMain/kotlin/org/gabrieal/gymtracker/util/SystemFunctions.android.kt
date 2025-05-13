package org.gabrieal.gymtracker.util

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri


@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.activity.compose.BackHandler(enabled = enabled, onBack = onBack)
}

@Composable
actual fun openURL(url: String) {
    val browserIntent = Intent(
        Intent.ACTION_VIEW,
        url.toUri()
    )
    LocalContext.current.startActivity(browserIntent)
}