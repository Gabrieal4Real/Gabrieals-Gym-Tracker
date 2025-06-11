package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.colors

actual object Loader {
    @Composable
    actual fun ShowDialog() {
        LoadingDialog(true)
    }

    @Composable
    actual fun HideDialog() {
        LoadingDialog(false)
    }

    @OptIn(ExperimentalMaterial3ExpressiveApi::class)
    @Composable
    fun LoadingDialog(boolean: Boolean) {
        if (boolean) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.black.copy(alpha = 0.5f))
                    .clickable(enabled = false) {}) {
                LoadingIndicator(
                    modifier = Modifier
                        .size(180.dp)
                        .align(Alignment.Center),
                    color = colors.bottomNavIndicator,
                )
            }
        }
    }
}