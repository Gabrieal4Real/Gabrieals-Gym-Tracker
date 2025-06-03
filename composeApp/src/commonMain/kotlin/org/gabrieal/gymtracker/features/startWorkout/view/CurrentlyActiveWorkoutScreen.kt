package org.gabrieal.gymtracker.features.startWorkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutViewModel

object CurrentlyActiveWorkoutScreen : Screen {
    private val viewModel = StartWorkoutViewModel()

    @Composable
    override fun Content() {
        Column(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.white),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

        }
    }
}