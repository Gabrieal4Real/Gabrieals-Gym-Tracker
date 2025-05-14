package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.new_to_workout
import org.gabrieal.gymtracker.ui.widgets.AnimatedImage
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.BigText
import org.gabrieal.gymtracker.ui.widgets.ConfirmButton
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.appUtil.Workout
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.days

object CreateSplitScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var selectedDays by remember { mutableStateOf(listOf(true, false, true, false, true, false, false)) }
        var showImage by rememberSaveable { mutableStateOf(true) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow(Resources.strings.createSplit)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.LighterBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                ) {
                    SubtitleText(Resources.strings.howManyDays)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Day Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        days.forEachIndexed { index, day ->
                            val isSelected = selectedDays[index]
                            Card(
                                shape = RoundedCornerShape(4.dp),
                                backgroundColor = if (isSelected) Colors.White else Colors.Maroon,
                                elevation = if (isSelected) 8.dp else 4.dp,
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        selectedDays = selectedDays.toMutableList().apply {
                                            // Limit to 5 active days
                                            if (!isSelected && this.count { it } >= 5) {
                                                this[this.indexOfFirst { it }] = false
                                            }
                                            this[index] = !isSelected
                                        }
                                    }
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BigText(day, color = if (isSelected) Colors.Black else Colors.White)
                                }
                            }
                        }
                    }

                    // Rest Day Indicator
                    DescriptionText(
                        Resources.strings.redsIndicateRest,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Recommended Split
                    SubtitleText(Resources.strings.recommendedSplit)
                    Spacer(modifier = Modifier.height(8.dp))
                    Workout.WorkoutSplit(selectedDays)
                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Confirm Button
                ConfirmButton(
                    Resources.strings.letsPlanIt,
                    onClick = { navigator.push(MakeAPlanScreen(selectedDays)) },
                    enabled = selectedDays.any { it },
                    modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                )

                // Animated Image
                showImage = AnimatedImage(showImage, Res.drawable.new_to_workout, false)
            }
        }
    }
}
