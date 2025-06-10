package org.gabrieal.gymtracker.features.startWorkout.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutUiState
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutViewModel
import org.gabrieal.gymtracker.startTime
import org.gabrieal.gymtracker.util.app.ElapsedTime
import org.gabrieal.gymtracker.util.app.getCurrentTimerInSeconds
import org.gabrieal.gymtracker.util.app.isValidDecimal
import org.gabrieal.gymtracker.util.app.isValidNumber
import org.gabrieal.gymtracker.util.systemUtil.notifyPlatform
import org.gabrieal.gymtracker.util.systemUtil.requestNotificationPermission
import org.gabrieal.gymtracker.util.widgets.BiggerText
import org.gabrieal.gymtracker.util.widgets.ClickToStartTimerBar
import org.gabrieal.gymtracker.util.widgets.ConfirmButton
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.CustomHorizontalDivider
import org.gabrieal.gymtracker.util.widgets.CustomSwitch
import org.gabrieal.gymtracker.util.widgets.CustomTextField
import org.gabrieal.gymtracker.util.widgets.CustomUnderlinedTextField
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import kotlin.time.ExperimentalTime

object CurrentlyActiveWorkoutBottomSheet : Screen {
    private val viewModel = StartWorkoutViewModel()

    fun setSelectedExerciseList(selectedExerciseList: SelectedExerciseList) {
        viewModel.setSelectedExerciseList(selectedExerciseList)
        viewModel.initializeCompletedSets(selectedExerciseList)
        viewModel.reset()
    }

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val selectedExerciseList = uiState.selectedExerciseList
        val completedVolume = uiState.completedVolume
        val completedSets = uiState.exerciseSets.sumOf { it -> it.count { it } }
        val showNotification = uiState.showNotification

        LaunchedEffect(key1 = showNotification) {
            requestNotificationPermission()

            if (showNotification) {
                notifyPlatform("Time is up!")
                viewModel.setShowNotification(false)
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BiggerText(selectedExerciseList?.routineName.orEmpty())
                    Spacer(modifier = Modifier.height(16.dp))
                    DurationVolumeSetCard(completedVolume, completedSets)
                    Spacer(modifier = Modifier.height(16.dp))

                    val addTime = listOf(
                        "30S" to 30,
                        "1M" to 60,
                        "2M" to 120,
                        "5M" to 300
                    )

                    ClickToStartTimerBar(
                        isRunning = uiState.isRunning,
                        currentTime = uiState.currentTime,
                        totalTime = uiState.totalTime,
                        onClick = { viewModel.startOrPauseTimer() },
                        onRestart = { viewModel.restartTimer() },
                        onReset = { viewModel.resetTimer() },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        addTime.forEachIndexed() { index, (label, seconds) ->
                            Box(
                                modifier = Modifier
                                    .height(42.dp)
                                    .weight(1f)
                                    .background(
                                        colors.placeholderColor,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .clickable { viewModel.addTime(seconds) },
                                contentAlignment = Alignment.Center
                            ) {
                                TinyText("+$label", color = colors.white)
                            }

                            if (index != addTime.lastIndex) {
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }

                CustomHorizontalDivider()

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.lighterBackground)
                        .padding(start = 16.dp, end = 16.dp),
                ) {
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                    selectedExerciseList?.exercises?.forEachIndexed { index, exercise ->
                        item {
                            ExerciseItem(index, exercise, uiState)
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    fun DurationVolumeSetCard(volume: Double, sets: Int) {
        val stats = listOf(
            "Duration" to ElapsedTime(startTime),
            "Volume" to "$volume kg",
            "Sets" to sets.toString()
        )

        CustomCard(enabled = false, content = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                stats.forEachIndexed { index, (label, value) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        TinyText(label)
                        DescriptionText(value)
                    }
                    if (index != stats.lastIndex) {
                        Spacer(modifier = Modifier.width(8.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }
            }
        })
    }

    @Composable
    fun ExerciseItem(index: Int, exercise: SelectedExercise, uiState: StartWorkoutUiState) {
        val expanded = uiState.expandedExercises[index]
        val weights = uiState.exerciseWeights[index]
        val reps = uiState.exerciseReps[index]
        val sets = uiState.exerciseSets[index]

        CustomCard(
            backgroundEnabled = true,
            enabled = true,
            onClick = { viewModel.toggleExerciseExpanded(index) },
            content = {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            DescriptionText(exercise.name.orEmpty())
                            if (!expanded) {
                                TinyText(
                                    "${sets.count { it }} / ${exercise.sets ?: 0} done",
                                    color = colors.textSecondary
                                )
                                return@Column
                            }
                        }
                        Icon(
                            imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = null,
                            tint = colors.white
                        )
                    }

                    if (expanded) {
                        TinyText(
                            "${exercise.reps?.first} to ${exercise.reps?.second} reps",
                            color = colors.textSecondary
                        )
                        Spacer(Modifier.height(16.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                TinyItalicText("Weight (kg)?")
                                Spacer(modifier = Modifier.height(8.dp))
                                CustomTextField(
                                    value = weights,
                                    onValueChange = {
                                        if (it.isValidDecimal() && it.length <= 5) viewModel.updateWeight(
                                            index,
                                            it
                                        )
                                    },
                                    placeholderText = "75.0"
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                TinyItalicText("Rest Timer")
                                Spacer(modifier = Modifier.height(8.dp))
                                ConfirmButton(
                                    text = if (uiState.isRunning) "Replace" else "Start",
                                    onClick = {
                                        viewModel.setTotalTime(getCurrentTimerInSeconds(exercise.reps))
                                        viewModel.startTimer()
                                    },
                                    modifier = Modifier.fillMaxWidth().height(56.dp)
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            listOf("Set", "", "Reps", "", "").zip(
                                listOf(
                                    0.4f,
                                    0.3f,
                                    1f,
                                    0.3f,
                                    0.5f
                                )
                            ).forEach { (label, weight) ->
                                TinyText(
                                    label.uppercase(),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(weight),
                                    color = colors.textSecondary
                                )
                            }
                        }

                        Spacer(Modifier.height(4.dp))

                        repeat(exercise.sets ?: 0) { pos ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DescriptionText(
                                    "${pos + 1}",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(0.5f)
                                )
                                Spacer(Modifier.weight(0.3f))
                                CustomUnderlinedTextField(
                                    keyboardType = KeyboardType.Number,
                                    value = reps[pos],
                                    onValueChange = {
                                        if (!sets[pos] && it.isValidNumber() && it.length <= 5)
                                            viewModel.updateReps(index, it, pos)
                                    },
                                    placeholderText = "-",
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(Modifier.weight(0.3f))
                                CustomSwitch(
                                    checked = sets[pos],
                                    onCheckedChange = {
                                        viewModel.updateExerciseSets(index, pos)
                                        viewModel.toggleSetCompleted()
                                    },
                                    modifier = Modifier.weight(0.5f)
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        )
        Spacer(Modifier.height(16.dp))
    }
}