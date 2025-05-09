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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.nothing_here
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.Navigator
import org.gabrieal.gymtracker.util.RegularText
import org.gabrieal.gymtracker.util.Screen
import org.gabrieal.gymtracker.ui.widgets.TitleText
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun HomeScreen() {
    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        TitleText(text = "Gabrieal's Gym Tracker")
        Box(modifier = Modifier.background(Colors.BorderStroke).fillMaxWidth().height(1.dp))
        Box(modifier = Modifier.fillMaxSize().background(Colors.LighterBackground).padding(16.dp)) {
            Column (modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Image(
                    painter = painterResource(Res.drawable.nothing_here),
                    contentDescription = "Nothing here yet",
                    modifier = Modifier.padding(16.dp).size(250.dp),
                )
                Column (
                    modifier = Modifier.clickable {
                        Navigator.navigateTo(Screen.WorkoutSplit)
                    }.padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
                )
                {
                    Text("Nothing here yet", style = TextStyle(fontFamily = RegularText()), color = Colors.TextSecondary, fontSize = 18.sp)
                    Text("Let's start tracking your workouts >", style = TextStyle(fontFamily = RegularText()), color = Colors.Link, fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}