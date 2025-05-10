package org.gabrieal.gymtracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.new_to_workout
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.ConfirmButton
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.ui.widgets.TinyText
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.Resources
import org.gabrieal.gymtracker.util.Workout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
fun WorkoutSplitScreen() {
    var animationVisibility by remember { mutableStateOf(true) }
    var sliderValue by remember { mutableStateOf(3f) }
    val scrollState = rememberScrollState()
    var hasScrolled by remember { mutableStateOf(false) }

    LaunchedEffect(scrollState.value) {
        if (scrollState.value != 0 && !hasScrolled) {
            hasScrolled = true
        }

        if (hasScrolled) {
            animationVisibility = false
        }
    }

    val minWorkouts = 1f
    val maxWorkouts = 5f

    val days = listOf("M", "T", "W", "T", "F", "S", "S")
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
                    SubtitleText("Here's how it works:")
                    Spacer(modifier = Modifier.height(4.dp))
                    Workout.getWorkoutSplit(selectedDays)
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
            Box(contentAlignment = Alignment.BottomEnd,
                modifier = Modifier.fillMaxSize().padding(16.dp)) {
                ConfirmButton(
                    text = "Let's Go Plan It!",
                    onClick = {
                        animationVisibility = false
                    }
                )
            }
            AnimatedImageVisibility(animationVisibility)
        }
    }
}

@Composable
fun AnimatedImageVisibility(isVisible: Boolean) {
    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.fillMaxSize().absoluteOffset(y = 4.dp)) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInHorizontally(animationSpec = tween(1000, easing = FastOutSlowInEasing)) { fullWidth -> fullWidth } + fadeIn(),
            exit = slideOutHorizontally(animationSpec = tween(1000, easing = FastOutSlowInEasing)) { fullWidth -> fullWidth } + fadeOut()
        ) {
            Image(
                painter = painterResource(Res.drawable.new_to_workout),
                contentDescription = "new_to_workout",
                modifier = Modifier
                    .size(350.dp)
            )
        }
    }
}