package org.gabrieal.gymtracker.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.gym_tracker_header
import org.gabrieal.gymtracker.util.Colors
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize().background(Colors.Background)
                .windowInsetsPadding(WindowInsets.safeDrawing),
            color = Colors.Background
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(Res.drawable.gym_tracker_header),
                    contentDescription = "Header",
                    modifier = Modifier.padding(16.dp)
                )

                Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    listOf(
                        Colors.Background,
                        Colors.CardBackground,
                        Colors.BorderStroke,
                        Colors.TextPrimary,
                        Colors.TextSecondary
                    ).forEach { color ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .background(color)
                                .padding(8.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}