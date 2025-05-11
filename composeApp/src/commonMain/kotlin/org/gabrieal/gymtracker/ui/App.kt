package org.gabrieal.gymtracker.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import io.github.alexzhirkevich.cupertino.adaptive.AdaptiveTheme
import io.github.alexzhirkevich.cupertino.adaptive.ExperimentalAdaptiveApi
import io.github.alexzhirkevich.cupertino.adaptive.Theme
import org.gabrieal.gymtracker.data.Exercise
import org.gabrieal.gymtracker.data.decodeExercises
import org.gabrieal.gymtracker.ui.screens.HomeScreen
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.LocalStringResources
import org.gabrieal.gymtracker.util.StringFactory
import org.gabrieal.gymtracker.util.StringResources
import org.gabrieal.gymtracker.util.language
import org.gabrieal.gymtracker.util.readFile
import org.jetbrains.compose.ui.tooling.preview.Preview

expect fun determineTheme(): Theme

var allExistingExerciseList = mutableListOf<Exercise>()

@OptIn(ExperimentalAdaptiveApi::class)
@Composable
@Preview
fun App(stringResources: StringResources = remember { StringFactory.createStrings(language) }, theme: Theme = determineTheme()) {
    CompositionLocalProvider(
        LocalStringResources provides stringResources
    ) {
        allExistingExerciseList = decodeExercises(readFile("exercises.json"))

        AdaptiveTheme(
            target = theme,
            content = {
                Surface(
                    modifier = Modifier.fillMaxSize().background(Colors.Background).windowInsetsPadding(WindowInsets.safeDrawing),
                    color = Colors.Background
                ) {
                    // Use the navigation system to determine which screen to show
                    Navigator(HomeScreen)
                }
            }
        )
    }
}