package org.gabrieal.gymtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.gabrieal.gymtracker.ui.screens.HomeScreen
import org.gabrieal.gymtracker.ui.screens.WorkoutSplitScreen
import org.gabrieal.gymtracker.util.BackHandler
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.LocalStringResources
import org.gabrieal.gymtracker.util.Navigator
import org.gabrieal.gymtracker.util.Screen
import org.gabrieal.gymtracker.util.StringFactory
import org.gabrieal.gymtracker.util.StringResources
import org.gabrieal.gymtracker.util.language
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(stringResources: StringResources = remember { StringFactory.createStrings(language) }) {
    CompositionLocalProvider(
        LocalStringResources provides stringResources
    ) {
        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize().background(Colors.Background).windowInsetsPadding(WindowInsets.safeDrawing),
                color = Colors.Background
            ) {
                // Handle back button presses
                BackHandler(enabled = Navigator.canNavigateBack()) {
                    Navigator.navigateBack()
                }

                // Use the navigation system to determine which screen to show
                when (Navigator.getCurrentScreen()) {
                    is Screen.Home -> HomeScreen()
                    is Screen.WorkoutSplit -> WorkoutSplitScreen()
                }
            }
        }
    }
}