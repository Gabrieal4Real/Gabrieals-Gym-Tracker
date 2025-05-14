package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.start_editing
import org.gabrieal.gymtracker.data.SelectedExerciseList
import org.gabrieal.gymtracker.ui.widgets.AnimatedImage
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.ui.widgets.TinyItalicText
import org.gabrieal.gymtracker.ui.widgets.TinyText
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.fullDays
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.getCurrentPlan

data class MakeAPlanScreen(val selectedDays: List<Boolean>) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var showImage by rememberSaveable { mutableStateOf(true) }
        var selectedRoutineList by rememberSaveable { mutableStateOf(mutableListOf<SelectedExerciseList>()) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow(Resources.strings.makeAPlan)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.LighterBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    SubtitleText(
                        Resources.strings.xDayWorkoutxDayRest(
                            selectedDays.count { it },
                            selectedDays.count { !it }
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Day Cards
                    fullDays.forEachIndexed { index, day ->
                        val isActive = selectedDays[index]
                        val hasExercises = selectedRoutineList.any { it.day == day }

                        Card(
                            shape = RoundedCornerShape(8.dp),
                            backgroundColor = if (isActive) Colors.CardBackground else Colors.Maroon,
                            border = BorderStroke(2.dp, Colors.BorderStroke),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .clickable(
                                    enabled = isActive,
                                    onClick = {
                                        val exercises = selectedRoutineList.find { it.day == day }?.exercises
                                        DayEditScreen.setSelectedDay(getCurrentPlan(selectedDays)[index])
                                        DayEditScreen.setExercises(exercises)
                                        DayEditScreen.setCallback { updatedExercises ->
                                            selectedRoutineList = selectedRoutineList
                                                .filterNot { it.day == day }
                                                .toMutableList()
                                                .apply {
                                                    add(SelectedExerciseList(day = day, exercises = updatedExercises))
                                                }
                                        }
                                        navigator.push(DayEditScreen)
                                    }
                                ).shadow(
                                    elevation = 4.dp,
                                    shape = RoundedCornerShape(8.dp),
                                    ambientColor = Colors.Black,
                                    spotColor = Colors.Black
                                )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column {
                                        DescriptionText(day)
                                        if (isActive) {
                                            TinyItalicText(getCurrentPlan(selectedDays)[index], color = Colors.TextSecondary)
                                            TinyText(if (hasExercises) Resources.strings.edited else Resources.strings.notEditedYet)
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    if (!isActive) {
                                        TinyItalicText(Resources.strings.restDay)
                                    } else {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = Colors.TextPrimary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Animated Image
                showImage = AnimatedImage(showImage, Res.drawable.start_editing, true)
            }
        }
    }
}
