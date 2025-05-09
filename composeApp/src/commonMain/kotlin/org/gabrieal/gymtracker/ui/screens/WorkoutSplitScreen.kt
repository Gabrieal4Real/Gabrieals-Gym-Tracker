package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.Colors
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun WorkoutSplitScreen() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        BackButtonRow(text = "Create Split")
        Box(modifier = Modifier.fillMaxSize().background(Colors.LighterBackground).padding(16.dp)) {
        }
    }
}