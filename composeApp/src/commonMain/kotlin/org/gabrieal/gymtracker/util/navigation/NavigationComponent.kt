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
        // Set the navigator instance in the AppNavigator
        LaunchedEffect(navigator) {
            AppNavigator.setNavigator(navigator)
        }
        
        // Observe navigation events and process them
        val navigationEvent by AppNavigator.navigationEvents.collectAsState()
        
        // Process navigation events when they occur
        LaunchedEffect(navigationEvent) {
            navigationEvent?.let { event ->
                AppNavigator.processNavigationEvent(event)
            }
        }
        
        // Display the current screen with a slide transition
        SlideTransition(navigator)
    }
}
