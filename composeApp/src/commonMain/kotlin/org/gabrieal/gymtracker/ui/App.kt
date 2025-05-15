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
import org.gabrieal.gymtracker.data.Exercise
import org.gabrieal.gymtracker.data.decodeExercises
import org.gabrieal.gymtracker.navigation.NavigationComponent
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.systemUtil.LocalStringResources
import org.gabrieal.gymtracker.util.systemUtil.StringFactory
import org.gabrieal.gymtracker.util.systemUtil.StringResources
import org.gabrieal.gymtracker.util.systemUtil.language
import org.gabrieal.gymtracker.util.systemUtil.readFile
import org.jetbrains.compose.ui.tooling.preview.Preview

expect fun isIOS(): Boolean

var allExistingExerciseList = mutableListOf<Exercise>()
@Composable
@Preview
fun App(stringResources: StringResources = remember { StringFactory.createStrings(language) }) {
    CompositionLocalProvider(
        LocalStringResources provides stringResources
    ) {
        allExistingExerciseList = decodeExercises(readFile("exercises.json"))

        MaterialTheme{
                Surface(
                    modifier = Modifier.fillMaxSize().background(Colors.Background).windowInsetsPadding(WindowInsets.safeDrawing),
                    color = Colors.Background
                ) {
                    NavigationComponent()
                }
            }
    }
}