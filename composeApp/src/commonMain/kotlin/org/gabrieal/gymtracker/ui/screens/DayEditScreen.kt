package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.gabrieal.gymtracker.data.decodeExercises
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.Workout.Companion.planTitles
import org.gabrieal.gymtracker.util.readFile

data class DayEditScreen(val selectedDay: String) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val exerciseList = decodeExercises(readFile("exercises.json"))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val selectedDay = planTitles.find { selectedDay.contains(it) }
            BackButtonRow(text = "$selectedDay Day")
            Box {
                Column(
                    modifier = Modifier.fillMaxSize().background(Colors.LighterBackground)
                        .padding(16.dp).verticalScroll(rememberScrollState()).clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {

                        },
                    )
                ) {

                }
            }
        }
    }
}