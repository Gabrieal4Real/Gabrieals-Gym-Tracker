package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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

data class SplitEditScreen(val selectedDays: List<Boolean>) : Screen {
    @Composable
    override fun Content() {
        var showImage = true
        val navigator = LocalNavigator.currentOrThrow

        var selectedRoutineList by rememberSaveable { mutableStateOf(mutableListOf<SelectedExerciseList>()) }

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            BackButtonRow(Resources.strings.makeAPlan)
            Box {
                Column(modifier = Modifier.fillMaxSize().background(Colors.LighterBackground).padding(16.dp)) {
                    SubtitleText(
                        Resources.strings.xDayWorkoutxDayRest(selectedDays.count { it }, selectedDays.count { !it }),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column (modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
                        repeat(7) { position ->
                            Card (shape = RoundedCornerShape(8.dp),
                                backgroundColor = if(selectedDays[position]) Colors.CardBackground else Colors.Maroon,
                                border = BorderStroke(2.dp, Colors.BorderStroke),
                                modifier = Modifier.padding(bottom = 8.dp).fillMaxSize().clickable(
                                    indication = if(selectedDays[position]) LocalIndication.current else null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        if (selectedDays[position]) {
                                            DayEditScreen.setSelectedDay(getCurrentPlan(selectedDays)[position])
                                            DayEditScreen.setExercises(
                                                if (selectedRoutineList.any { it.day == fullDays[position]}) {
                                                    val index = selectedRoutineList.indexOfFirst { it.day == fullDays[position] }
                                                    selectedRoutineList[index].exercises
                                                } else {
                                                    null
                                                }
                                            )
                                            DayEditScreen.setCallback { selectedExerciseList ->
                                                if (selectedRoutineList.any { it.day == fullDays[position] }) {
                                                    val index = selectedRoutineList.indexOfFirst { it.day == fullDays[position] }
                                                    selectedRoutineList.removeAt(index)
                                                }

                                                selectedRoutineList.add(
                                                    SelectedExerciseList(
                                                        day = fullDays[position],
                                                        exercises = selectedExerciseList
                                                    )
                                                )
                                            }
                                            navigator.push(DayEditScreen)
                                        }
                                    },
                                )) {
                                Column (modifier = Modifier.padding(16.dp)) {
                                    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Column {
                                            DescriptionText(fullDays[position])
                                            if (selectedDays[position]) {
                                                TinyItalicText(getCurrentPlan(selectedDays)[position], color = Colors.TextSecondary)
                                                if (selectedRoutineList.any { it.day == fullDays[position] }) TinyText(Resources.strings.edited)
                                                else TinyText(Resources.strings.notEditedYet)
                                            }
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (!selectedDays[position]) {
                                            TinyItalicText(Resources.strings.restDay)
                                        } else {
                                            Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = Colors.TextPrimary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                showImage = AnimatedImage(showImage, Res.drawable.start_editing, true)
            }
        }
    }
}