package org.gabrieal.gymtracker.ui.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.nothing_here
import org.gabrieal.gymtracker.ui.widgets.LinkText
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.ui.widgets.TitleText
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.Resources
import org.jetbrains.compose.resources.painterResource

object HomeScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            TitleText(Resources.strings.appName)
            Box(modifier = Modifier.background(Colors.BorderStroke).fillMaxWidth().height(1.dp))
            Box(modifier = Modifier.fillMaxSize().background(Colors.LighterBackground).padding(16.dp)) {
                Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Image(
                        painter = painterResource(Res.drawable.nothing_here),
                        contentDescription = "Nothing here yet",
                        modifier = Modifier.padding(16.dp).size(250.dp),
                    )
                    Column (
                        modifier = Modifier.padding(8.dp).clickable {
                            navigator.push(SplitCreateScreen)
                        },
                        horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                    )
                    {
                        SubtitleText(Resources.strings.nothingHereYet)
                        LinkText(Resources.strings.startTrackingWorkout, modifier = Modifier.padding(top = 4.dp))
                    }
                }
            }
        }
    }
}