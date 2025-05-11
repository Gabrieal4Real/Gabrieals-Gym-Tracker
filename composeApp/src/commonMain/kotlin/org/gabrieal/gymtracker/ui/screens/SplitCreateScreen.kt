package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.new_to_workout
import kotlinx.coroutines.delay
import org.gabrieal.gymtracker.ui.widgets.AnimatedImageFromRightVisibility
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.ConfirmButton
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.ui.widgets.TinyText
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.Resources
import org.gabrieal.gymtracker.util.Workout
import org.gabrieal.gymtracker.util.Workout.Companion.days

object SplitCreateScreen : Screen {
    val minWorkouts = 1f
    val maxWorkouts = 5f

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var animationVisibility by rememberSaveable { mutableStateOf(false) }
        var sliderValue by rememberSaveable { mutableStateOf(3f) }
        val scrollState = rememberScrollState()
        var hasScrolled by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(200)
            animationVisibility = true
        }

        LaunchedEffect(scrollState.value) {
            if (scrollState.value != 0 && !hasScrolled) {
                hasScrolled = true
            }

            if (hasScrolled) {
                animationVisibility = false
            }
        }

        var selectedDays by remember { mutableStateOf(Workout.selectDays(sliderValue)) }

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            BackButtonRow(text = Resources.strings.createSplit)
            Box {
                Column (
                    modifier = Modifier.fillMaxSize().background(Colors.LighterBackground).padding(16.dp).clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onClick = {
                            animationVisibility = false
                        },
                    )){
                    Column (modifier = Modifier.fillMaxHeight(0.9f).verticalScroll(scrollState)){
                        SubtitleText(Resources.strings.howManyDays)
                        Spacer(modifier = Modifier.height(8.dp))
                        Slider(
                            value = sliderValue,
                            onValueChange = {
                                sliderValue = it
                                selectedDays = Workout.selectDays(sliderValue)
                                animationVisibility = false
                            },
                            valueRange = minWorkouts..maxWorkouts,
                            steps = 3,
                            colors = SliderDefaults.colors(
                                thumbColor = Colors.LinkBlue,
                                activeTrackColor = Colors.TextPrimary,
                                inactiveTrackColor = Colors.BorderStroke
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TinyText(minWorkouts.toInt().toString())
                            DescriptionText(
                                Resources.strings.xAmountOfSplit(
                                    sliderValue.toInt().toString()
                                )
                            )
                            TinyText(maxWorkouts.toInt().toString())
                        }
                        Spacer(modifier = Modifier.height(40.dp))
                        SubtitleText(Resources.strings.recommendedSplit)
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
                                        .size(32.dp)
                                        .background(
                                            color = if (!selectedDays[index]) Colors.Maroon else Colors.White,
                                            shape = RoundedCornerShape(4.dp)
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = day,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
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
                        SubtitleText(Resources.strings.heresHowItWorks)
                        Spacer(modifier = Modifier.height(4.dp))
                        Workout.WorkoutSplit(selectedDays)
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
                Box(contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    ConfirmButton(
                        text = Resources.strings.letsPlanIt,
                        onClick = {
                            animationVisibility = false
                            navigator.push(SplitEditScreen(selectedDays))
                        }
                    )
                }
                AnimatedImageFromRightVisibility(animationVisibility, Res.drawable.new_to_workout)
            }
        }
    }


}