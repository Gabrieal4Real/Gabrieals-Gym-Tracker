package org.gabrieal.gymtracker.views

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
import org.gabrieal.gymtracker.model.ConvertedTemplate
import org.gabrieal.gymtracker.model.Exercise
import org.gabrieal.gymtracker.model.decodeExercises
import org.gabrieal.gymtracker.model.decodeTemplate
import org.gabrieal.gymtracker.util.appUtil.AppColors
import org.gabrieal.gymtracker.util.appUtil.DarkColors
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.LocalStringResources
import org.gabrieal.gymtracker.util.systemUtil.NotificationPermissionHandler
import org.gabrieal.gymtracker.util.systemUtil.StringFactory
import org.gabrieal.gymtracker.util.systemUtil.StringResources
import org.gabrieal.gymtracker.util.systemUtil.language
import org.gabrieal.gymtracker.util.systemUtil.readFile
import org.gabrieal.gymtracker.views.screens.LandingScreen
import org.jetbrains.compose.ui.tooling.preview.Preview

expect fun isIOS(): Boolean

var allExistingExerciseList = mutableListOf<Exercise>()
var templates = ConvertedTemplate()
var colors: AppColors = DarkColors
lateinit var notificationPermissionHandler: NotificationPermissionHandler

@Composable
@Preview
fun App(permissionHandler: NotificationPermissionHandler, stringResources: StringResources = remember { StringFactory.createStrings(language) }) {

    CompositionLocalProvider(
        LocalStringResources provides stringResources
    ) {
        notificationPermissionHandler = permissionHandler
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
                    }

                    val navigationEvent by AppNavigator.navigationEvents.collectAsState()

                    LaunchedEffect(navigationEvent) {
                        navigationEvent?.let { event ->
                            AppNavigator.processNavigationEvent(event)
                        }
                    }

                    SlideTransition(navigator)
                }
            }
        }
    }
}