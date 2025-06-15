package org.gabrieal.gymtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.gabrieal.gymtracker.data.model.ConvertedTemplate
import org.gabrieal.gymtracker.data.model.Exercise
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.WorkoutProgress
import org.gabrieal.gymtracker.data.model.decodeExercises
import org.gabrieal.gymtracker.data.model.decodeTemplate
import org.gabrieal.gymtracker.features.app.viewmodel.AppStateViewModel
import org.gabrieal.gymtracker.features.landing.view.LandingScreen
import org.gabrieal.gymtracker.util.app.AppColors
import org.gabrieal.gymtracker.util.app.DarkColors
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.Loader
import org.gabrieal.gymtracker.util.systemUtil.LocalStringResources
import org.gabrieal.gymtracker.util.systemUtil.StringFactory
import org.gabrieal.gymtracker.util.systemUtil.StringResources
import org.gabrieal.gymtracker.util.systemUtil.language
import org.gabrieal.gymtracker.util.systemUtil.readFile
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

var allExistingExerciseList = mutableListOf<Exercise>()
var templates = ConvertedTemplate()
var colors: AppColors = DarkColors

@OptIn(ExperimentalTime::class)
var currentlyActiveRoutine: Triple<SelectedExerciseList, Instant, WorkoutProgress>? = null

val appStateViewModel = AppStateViewModel()

@Composable
@Preview
fun App(stringResources: StringResources = remember { StringFactory.createStrings(language) }) {
    CompositionLocalProvider(
        LocalStringResources provides stringResources
    ) {
        val uiState by appStateViewModel.uiState.collectAsState()
        val isLoading = uiState.isLoading

        allExistingExerciseList = decodeExercises(readFile("exercises.json"))
        templates = decodeTemplate(readFile("templates.json"))

        MaterialTheme {
            Surface(
                modifier = Modifier.fillMaxSize().background(colors.background)
                    .windowInsetsPadding(WindowInsets.safeDrawing),
                color = colors.background
            ) {
                Navigator(LandingScreen) { navigator ->
                    LaunchedEffect(navigator) {
                        AppNavigator.setNavigator(navigator)
                        AppNavigator.setAppStateViewModel(appStateViewModel)
                    }

                    val navigationEvent by AppNavigator.navigationEvents.collectAsState()

                    LaunchedEffect(navigationEvent) {
                        navigationEvent?.let { event ->
                            AppNavigator.processNavigationEvent(event)
                        }
                    }

                    SlideTransition(navigator)
                }

                if (isLoading) {
                    Loader.ShowDialog()
                } else {
                    Loader.HideDialog()
                }
            }
        }
    }
}