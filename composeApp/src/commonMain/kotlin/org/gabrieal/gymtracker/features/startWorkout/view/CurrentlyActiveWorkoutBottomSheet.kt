package org.gabrieal.gymtracker.features.startWorkout.view

import androidx.compose.foundation.background
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
        val totalSets = selectedExerciseList?.exercises?.sumOf { it.sets ?: 0 }

        Box(
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    BiggerText(selectedExerciseList?.routineName.orEmpty())

                    Spacer(modifier = Modifier.height(8.dp))

                    DurationVolumeSetCard(completedVolume, completedSets)

                    Spacer(modifier = Modifier.height(16.dp))

                    ClickToStartTimerBar(
                        isRunning = uiState.isRunning,
                        currentTime = uiState.currentTime,
                        totalTime = uiState.totalTime,
                        onClick = { viewModel.startOrPauseTimer() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                CustomHorizontalDivider()

                Column(modifier = Modifier.fillMaxSize().background(colors.lighterBackground)) {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth()
                            .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        selectedExerciseList?.exercises?.let {
                            items(it.size) { index ->
                                ExerciseItem(index, it[index], uiState)
                            }
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    fun DurationVolumeSetCard(
        completedVolume: Double,
        completedSets: Int
    ) {
        CustomCard(
            enabled = false,
            content = {
                val titleMessage = listOf(
                    Pair("Duration", ElapsedTime(startTime)),
                    Pair("Volume", "$completedVolume kg"),
                    Pair("Sets", completedSets)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    titleMessage.forEachIndexed { index, _ ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            TinyText(titleMessage[index].first)
                            DescriptionText(titleMessage[index].second.toString())
                        }

                        if (index != titleMessage.lastIndex) {
                            Spacer(modifier = Modifier.width(8.dp))
                            DashedDivider()
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun ExerciseItem(index: Int, exercise: SelectedExercise, uiState: StartWorkoutUiState) {
        val exerciseName = exercise.name.orEmpty()
        val setCount = exercise.sets ?: 0
        val expanded = uiState.expandedExercises[index]
        val exerciseWeights = uiState.exerciseWeights[index]
        val exerciseReps = uiState.exerciseReps[index]
        val exerciseSets = uiState.exerciseSets[index]
        val completedSets = uiState.exerciseSets[index].count { it }
        val isTimerRunning = uiState.isRunning

        CustomCard(
            backgroundEnabled = true,
            enabled = true,
            onClick = { viewModel.toggleExerciseExpanded(index) },
            content = {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    // Header
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            DescriptionText(exerciseName)
                            if (!expanded) {
                                TinyText(
                                    "$completedSets / $setCount done",
                                    color = colors.textSecondary
                                )
                                return@Column
                            }
                        }
                        Icon(
                            imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Toggle",
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
                                    value = exerciseWeights,
                                    onValueChange = { weight ->
                                        if (weight.isValidDecimal() && weight.length <= 5)
                                            viewModel.updateWeight(index, weight)
                                    },
                                    placeholderText = "75.0",
                                )
                            }

                            Spacer(Modifier.width(16.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                TinyItalicText("Rest Timer")
                                Spacer(modifier = Modifier.height(8.dp))
                                ConfirmButton(
                                    text = if (isTimerRunning) "Replace" else "Start",
                                    onClick = {
                                        viewModel.setTotalTime(getCurrentTimerInSeconds(exercise.reps))
                                        viewModel.startTimer()
                                    },
                                    modifier = Modifier.fillMaxWidth().height(56.dp),
                                )
                            }
                        }
                        Spacer(Modifier.height(16.dp))

                        // Headers
                        Row(modifier = Modifier.fillMaxWidth()) {
                            listOf("Set", "", "Reps", "", "").zip(
                                listOf(
                                    0.4f,
                                    0.3f,
                                    1f,
                                    0.3f,
                                    0.5f
                                )
                            )
                                .forEach { (label, weight) ->
                                    TinyText(
                                        label.uppercase(),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(weight),
                                        color = colors.textSecondary
                                    )
                                }
                        }

                        Spacer(Modifier.height(4.dp))

                        repeat(setCount) { position ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DescriptionText(
                                    "${position + 1}",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(0.5f)
                                )
                                Spacer(Modifier.weight(0.3f))

                                CustomUnderlinedTextField(
                                    keyboardType = KeyboardType.Number,
                                    value = exerciseReps[position],
                                    onValueChange = { reps ->
                                        if (!exerciseSets[position] && reps.isValidNumber() && reps.length <= 5)
                                            viewModel.updateReps(index, reps, position)
                                    },
                                    placeholderText = "-",
                                    modifier = Modifier.weight(1f)
                                )

                                Spacer(Modifier.weight(0.3f))

                                CustomSwitch(
                                    checked = exerciseSets[position],
                                    onCheckedChange = {
                                        viewModel.updateExerciseSets(index, position)
                                        viewModel.toggleSetCompleted()
                                    },
                                    modifier = Modifier.weight(0.5f),
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