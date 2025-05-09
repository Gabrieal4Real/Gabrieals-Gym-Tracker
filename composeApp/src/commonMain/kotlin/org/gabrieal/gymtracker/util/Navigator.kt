package org.gabrieal.gymtracker.util

import androidx.compose.runtime.snapshots.SnapshotStateList

sealed class Screen {
    data object Home : Screen()
    data object WorkoutSplit : Screen()
}

class Navigator {
    companion object {
        // Stack of screens with the current screen at the top
        private val screenStack = SnapshotStateList<Screen>().apply {
            add(Screen.Home) // Start with Home screen
        }

        // Current screen is always the top of the stack
        fun getCurrentScreen(): Screen {
            return screenStack.lastOrNull() ?: Screen.Home
        }

        // Navigate to a new screen by adding it to the top of the stack
        fun navigateTo(screen: Screen) {
            screenStack.add(screen)
        }

        // Navigate back by removing the top screen from the stack
        fun navigateBack(): Boolean {
            if (screenStack.size > 1) {
                screenStack.removeAt(screenStack.lastIndex)
                return true
            }
            return false // Can't go back further
        }

        // Check if we can navigate back
        fun canNavigateBack(): Boolean {
            return screenStack.size > 1
        }

        // Clear back stack and set a new root screen
        fun navigateToRoot(rootScreen: Screen = Screen.Home) {
            screenStack.clear()
            screenStack.add(rootScreen)
        }
    }
}
