package org.gabrieal.gymtracker.views.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.nothing_here
import org.gabrieal.gymtracker.views.widgets.LinkText
import org.gabrieal.gymtracker.views.widgets.SubtitleText
import org.gabrieal.gymtracker.views.widgets.TitleText
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.viewmodel.HomeViewModel
import org.jetbrains.compose.resources.painterResource

object HomeScreen : Screen {
    // Create a single instance of the ViewModel
    private val viewModel = HomeViewModel()
    
    @Composable
    override fun Content() {
        // Collect the UI state from the ViewModel
        val uiState by viewModel.uiState.collectAsState()

        // Get the current context and update the ViewModel
        val context = getCurrentContext()
        LaunchedEffect(context) {
            viewModel.updateContext(context)
        }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(Resources.strings.appName)

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Colors.BorderStroke)
                    .height(1.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.LighterBackground)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(Res.drawable.nothing_here),
                        contentDescription = "Nothing here yet",
                        modifier = Modifier
                            .padding(16.dp)
                            .size(250.dp)
                    )

                    Column(
                        modifier = Modifier
                            .clickable { viewModel.navigateToCreateSplit() }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SubtitleText(Resources.strings.nothingHereYet)
                        LinkText(Resources.strings.startTrackingWorkout, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}
