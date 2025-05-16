package org.gabrieal.gymtracker.util.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.gabrieal.gymtracker.views.screens.HomeScreen

@Composable
fun NavigationComponent() {
    Navigator(HomeScreen) { navigator ->
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
