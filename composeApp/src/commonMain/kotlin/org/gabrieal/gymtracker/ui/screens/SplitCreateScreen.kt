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
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import org.gabrieal.gymtracker.ui.widgets.TinyText
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.appUtil.Workout
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.days

object SplitCreateScreen : Screen {
    @Composable
    override fun Content() {
        var showImage = true
        val navigator = LocalNavigator.currentOrThrow

        var selectedDays by remember { mutableStateOf(listOf(true, false, true, false, true, false, false)) }

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            BackButtonRow(Resources.strings.createSplit)
            Box {
                Column (
                    modifier = Modifier.fillMaxSize().background(Colors.LighterBackground).padding(16.dp)){
                    Column (modifier = Modifier.fillMaxHeight(0.9f).verticalScroll(rememberScrollState())){
                        SubtitleText(Resources.strings.howManyDays)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            days.forEachIndexed { index, day ->
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(
                                            color = if (!selectedDays[index]) Colors.Maroon else Colors.White,
                                            shape = RoundedCornerShape(4.dp)
                                        ).clickable {
                                            if (selectedDays.count { it } >= 5 && !selectedDays[index]) {
                                                selectedDays.find { it }?.let {
                                                    selectedDays = selectedDays.toMutableList().apply {
                                                        this[selectedDays.indexOf(it)] = false
                                                    }
                                                }

                                                selectedDays = selectedDays.toMutableList().apply {
                                                    this[index] = !selectedDays[index]
                                                }

                                                return@clickable
                                            }
                                            selectedDays = selectedDays.toMutableList().apply {
                                                this[index] = !selectedDays[index]
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    BigText(
                                        day,
                                        color = if (!selectedDays[index]) Colors.White else Colors.Black
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            DescriptionText(Resources.strings.redsIndicateRest)
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        SubtitleText(Resources.strings.recommendedSplit)
                        Spacer(modifier = Modifier.height(8.dp))
                        Workout.WorkoutSplit(selectedDays)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                Box(contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    ConfirmButton(
                        Resources.strings.letsPlanIt,
                        onClick = {
                            navigator.push(SplitEditScreen(selectedDays))
                        },
                        enabled = selectedDays.count { it } >= 1,
                    )
                }
                showImage = AnimatedImage(showImage, Res.drawable.new_to_workout, false)
            }
        }
    }


}