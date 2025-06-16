package org.gabrieal.gymtracker.util.systemUtil

import androidx.compose.runtime.Composable

expect object Loader {
    @Composable
    fun ShowDialog()

    @Composable
    fun HideDialog()
}