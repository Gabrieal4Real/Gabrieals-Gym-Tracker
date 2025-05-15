package org.gabrieal.gymtracker.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.start_editing
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.data.SelectedExerciseList
import org.gabrieal.gymtracker.ui.widgets.AnimatedImage
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.ConfirmButton
import org.gabrieal.gymtracker.ui.widgets.CustomCard
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.ui.widgets.TinyItalicText
import org.gabrieal.gymtracker.ui.widgets.TinyText
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.fullDays
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.getCurrentPlan
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.util.systemUtil.providePreferences

object MakeAPlanScreen : Screen {
    var selectedDays: List<Boolean> = listOf()

    // Set the day being edited
    fun setSelectedDay(selectedDays: List<Boolean>) {
        this.selectedDays = selectedDays
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var showImage by rememberSaveable { mutableStateOf(true) }
        var selectedRoutineList by rememberSaveable { mutableStateOf(mutableListOf<SelectedExerciseList>()) }
        var showWarningBack by rememberSaveable { mutableStateOf(false) }
        var saveRoutineList by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow(Resources.strings.makeAPlan) {
                if (selectedRoutineList.isNotEmpty()) {
                    showWarningBack = true
                } else {
                    navigator.pop()
                }
            }

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
                        CustomCard(
                            enabled = isActive,
                            onClick = {
                                val exercises = selectedRoutineList.find { it.day == day }?.exercises
                                EditPlanScreen.setSelectedDay(getCurrentPlan(selectedDays)[index])
                                EditPlanScreen.setExercises(exercises)
                                EditPlanScreen.setCallback { updatedExercises ->
                                    selectedRoutineList = selectedRoutineList
                                        .filterNot { it.day == day }
                                        .toMutableList()
                                        .apply {
                                            add(SelectedExerciseList(position = index, day = day, exercises = updatedExercises))
                                        }
                                }
                                navigator.push(EditPlanScreen)
                            },
                            content = {
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
                        })
                    }
                }

                // Confirm Button
                if (selectedDays.count { it } == selectedRoutineList.size) {
                    ConfirmButton(
                        "Let's Pump It Up",
                        onClick = {
                            saveRoutineList = true
                        },
                        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                    )
                }

                // Animated Image
                showImage = AnimatedImage(showImage, Res.drawable.start_editing, true)

                if (showWarningBack) {
                    ShowAlertDialog(
                        titleMessage = Pair(
                            "Are you sure?",
                            "You will lose all previous changes"
                        ),
                        positiveButton = Pair("Proceed") {
                            navigator.pop()
                            showWarningBack = false
                        },
                        negativeButton = Pair("Cancel") {
                            showWarningBack = false
                        }
                    )
                }

                if (saveRoutineList) {
                    selectedRoutineList = selectedRoutineList.sortedBy { it.position }.toMutableList()

                    getCurrentContext().let {
                        providePreferences(it).putString("selectedRoutineList", Json.encodeToString(selectedRoutineList))
                        navigator.popUntilRoot()
                        saveRoutineList = false
                    }
                }
            }
        }
    }
}
