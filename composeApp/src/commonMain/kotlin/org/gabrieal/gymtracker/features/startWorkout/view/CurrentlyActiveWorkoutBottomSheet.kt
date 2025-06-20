package org.gabrieal.gymtracker.features.startWorkout.view

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import network.chaintech.cmpcharts.common.extensions.roundTwoDecimal
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.currentlyActiveRoutine
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.sqldelight.getCurrentlyActiveRoutineFromDB
import org.gabrieal.gymtracker.data.sqldelight.updateWorkoutHistoryDB
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutUiState
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutViewModel
import org.gabrieal.gymtracker.util.app.ElapsedTime
import org.gabrieal.gymtracker.util.app.formatRestTime
import org.gabrieal.gymtracker.util.app.getCurrentTimerInSeconds
import org.gabrieal.gymtracker.util.app.isValidDecimal
import org.gabrieal.gymtracker.util.app.isValidNumber
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.systemUtil.notifyPlatform
import org.gabrieal.gymtracker.util.systemUtil.requestNotificationPermission
import org.gabrieal.gymtracker.util.widgets.ClickToStartTimerBar
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.CustomGrabber
import org.gabrieal.gymtracker.util.widgets.CustomHorizontalDivider
import org.gabrieal.gymtracker.util.widgets.CustomSwitch
import org.gabrieal.gymtracker.util.widgets.CustomTextField
import org.gabrieal.gymtracker.util.widgets.CustomUnderlinedTextField
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.RotatingExpandIcon
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object CurrentlyActiveWorkoutBottomSheet : Screen, KoinComponent {
    private val viewModel: StartWorkoutViewModel by inject()

    @OptIn(ExperimentalTime::class)
    fun setSelectedExerciseList(selectedExerciseList: SelectedExerciseList) {
        viewModel.initializeCompletedSets(selectedExerciseList)
        currentlyActiveRoutine = getCurrentlyActiveRoutineFromDB()
        currentlyActiveRoutine?.third?.let { viewModel.setWorkoutProgress(it) }
        viewModel.toggleSetCompleted()
    }

    fun setSuccessCallback(callback: (SelectedExerciseList) -> Unit) {
        viewModel.setCallback(callback)
    }

    fun setFailureCallback(failureCallback: () -> Unit) {
        viewModel.setFailureCallback(failureCallback)
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val selectedExerciseList = uiState.selectedExerciseList
        val completedVolume = uiState.completedVolume
        val totalSets = selectedExerciseList?.exercises?.sumOf { it.sets ?: 0 } ?: 0
        val completedSets = uiState.workoutProgress.exerciseSets.sumOf { it -> it.count { it } }
        val showNotification = uiState.showNotification
        val showWarningReplace = uiState.showWarningReplace
        val previousWeights = viewModel.getPreviousWorkout()
        val previousWeightUnit = viewModel.getPreviousWeightUnit()

        LaunchedEffect(key1 = showNotification) {
            requestNotificationPermission()

            if (showNotification) {
                notifyPlatform("Time is up!")
                viewModel.setShowNotification(false)
            }
        }

        if (showWarningReplace) {
            ShowAlertDialog(
                titleMessage = Pair(
                    "Workout in Progress",
                    "Some sets are not completed, are you sure you want to proceed?"
                ),
                positiveButton = Pair("Proceed") {
                    updateWorkoutHistoryDB(completedVolume)
                    viewModel.setShowWarningReplace(false)
                    viewModel.markWorkoutAsDone()
                    viewModel.resetTimer()
                    viewModel.startWorkout(null)
                },
                negativeButton = Pair("Cancel") {
                    viewModel.setShowWarningReplace(false)
                }
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.992f)
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomGrabber(modifier = Modifier.align(Alignment.CenterHorizontally))

                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AssistChip(
                            modifier = Modifier.weight(0.38f),
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = colors.deleteRed
                            ),
                            border = null,
                            onClick = {
                                viewModel.startWorkout(null)
                                viewModel.markWorkoutAsCancelled()
                            },
                            label = {
                                TinyText(
                                    text = "Cancel",
                                    color = colors.white,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            },
                        )
                        SubtitleText(
                            selectedExerciseList?.routineName.orEmpty().uppercase(),
                            color = colors.white,
                            modifier = Modifier.padding(horizontal = 2.dp).weight(1f),
                            textAlign = TextAlign.Center
                        )
                        AssistChip(
                            modifier = Modifier.weight(0.38f),
                            enabled = completedSets > 0,
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = if (completedSets == totalSets) colors.slightlyDarkerLinkBlue else colors.checkMarkGreen,
                                disabledContainerColor = colors.white.copy(alpha = 0.2f),
                            ),
                            border = null,
                            onClick = {
                                if (completedSets < totalSets) {
                                    viewModel.setShowWarningReplace(true)
                                    return@AssistChip
                                }
                                updateWorkoutHistoryDB(completedVolume)
                                viewModel.markWorkoutAsDone()
                                viewModel.resetTimer()
                                viewModel.startWorkout(null)
                            },
                            label = {
                                TinyText(
                                    text = "Finish",
                                    color = colors.white,
                                    modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth(),
                                    textAlign = TextAlign.Center
                                )
                            },
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    DurationVolumeSetCard(
                        completedVolume,
                        completedSets,
                        currentlyActiveRoutine?.second
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.lighterBackground),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    selectedExerciseList?.exercises?.forEachIndexed { index, exercise ->
                        item {
                            ExerciseItem(
                                index,
                                exercise,
                                uiState,
                                previousWeights,
                                previousWeightUnit
                            )
                        }
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    fun DurationVolumeSetCard(volume: Double, sets: Int, elapsedTime: Instant?) {
        val stats = listOf(
            "Duration" to ElapsedTime(elapsedTime),
            "Volume" to "$volume kg",
            "Sets" to sets.toString()
        )

        CustomCard(enabled = false, content = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                stats.forEachIndexed { index, (label, value) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f)
                    ) {
                        TinyText(label)
                        DescriptionText(value)
                    }
                    if (index != stats.lastIndex) {
                        Spacer(modifier = Modifier.width(4.dp))
                        DashedDivider()
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }
            }
        })
    }

    @Composable
    fun ExerciseItem(
        index: Int,
        exercise: SelectedExercise,
        uiState: StartWorkoutUiState,
        previousWeights: List<String>,
        previousWeightUnits: List<Boolean>
    ) {
        val expanded = uiState.expandedExercises[index]
        val weights = uiState.workoutProgress.exerciseWeights[index]
        val reps = uiState.workoutProgress.exerciseReps[index]
        val sets = uiState.workoutProgress.exerciseSets[index]
        val weightUnit = uiState.workoutProgress.exerciseWeightUnit[index]
        val previousWeightUnit = previousWeightUnits.getOrNull(index)
            ?.let { if (!previousWeightUnits[index]) "LB" else "KG" } ?: "KG"

        val previousWeight = previousWeights.getOrNull(index)
            ?.takeIf { it.isNotBlank() }
            ?.let { "$it $previousWeightUnit" } ?: "N/A"

        CustomCard(
            backgroundEnabled = !expanded,
            enabled = true,
            onClick = { viewModel.toggleExerciseExpanded(index) },
            content = {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .animateContentSize()
                        .padding(16.dp)
                ) {
                    Row(Modifier.fillMaxWidth()) {
                        Column(Modifier.weight(1f)) {
                            DescriptionText(exercise.name.orEmpty())
                            if (!expanded) {
                                TinyText(
                                    "${sets.count { it }} / ${exercise.sets ?: 0} done",
                                    color = colors.textSecondary
                                )
                            }
                        }
                        RotatingExpandIcon(expanded = expanded)
                    }

                    if (expanded) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Spacer(Modifier.height(4.dp))

                            Row(
                                Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(Modifier.weight(1f)) {
                                    TinyItalicText("Previous weight")
                                    DescriptionText(previousWeight)
                                }

                                Spacer(Modifier.width(8.dp))

                                Column(Modifier.weight(0.5f)) {
                                    TinyItalicText("Rest")
                                    DescriptionText(formatRestTime(getCurrentTimerInSeconds(exercise.reps)))
                                }

                                Spacer(Modifier.width(8.dp))

                                Column(Modifier.weight(0.5f)) {
                                    TinyItalicText("Rep Range")
                                    DescriptionText("${exercise.reps?.first} to ${exercise.reps?.second}")
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            TinyItalicText("Weight", modifier = Modifier.align(Alignment.Start))
                            Spacer(Modifier.height(8.dp))
                            Box {
                                CustomTextField(
                                    value = weights,
                                    onValueChange = {
                                        if (it.isValidDecimal() && it.split(".").first().length <= 5) {
                                            viewModel.updateWeight(index, it)
                                        }
                                    },
                                    placeholderText = "75.0",
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                )

                                Row(modifier = Modifier.align(Alignment.CenterEnd).padding(end = 1.dp)) {
                                    Button(
                                        contentPadding = PaddingValues(8.dp),
                                        shape = RoundedCornerShape(topEnd = 11.dp, bottomEnd = 11.dp),
                                        onClick = {
                                            if (weights.isNotEmpty() && weights.substringBefore(".").length <= 5) {
                                                val factor = if (weightUnit) 2.205 else 1 / 2.205
                                                val converted = (weights.toDouble() * factor).roundTwoDecimal()
                                                viewModel.updateWeight(index, converted.toString().removeSuffix(".0"))
                                            }

                                            viewModel.toggleWeightUnit(index)
                                            viewModel.toggleSetCompleted()
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = colors.slightlyDarkerLinkBlue,
                                            contentColor = colors.white,
                                        ),
                                        modifier = Modifier.height(54.dp)
                                    ) {
                                        DescriptionText(text = if (weightUnit) "KG" else "LB")
                                    }
                                }
                            }

                            Spacer(Modifier.height(20.dp))

                            Row(Modifier.fillMaxWidth()) {
                                listOf("Set", "|", "Reps", "|", "").zip(
                                    listOf(0.5f, 0.3f, 1f, 0.3f, 0.5f)
                                ).forEach { (label, weight) ->
                                    TinyText(
                                        label.uppercase(),
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(weight),
                                        color = colors.textSecondary
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))
                            CustomHorizontalDivider(0.95f)
                            Spacer(Modifier.height(4.dp))

                            repeat(exercise.sets ?: 0) { pos ->
                                Row(
                                    Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    DescriptionText(
                                        "${pos + 1}",
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.weight(0.5f)
                                    )

                                    Spacer(Modifier.weight(0.3f))

                                    CustomUnderlinedTextField(
                                        enabled = !sets[pos],
                                        keyboardType = KeyboardType.Number,
                                        value = reps[pos],
                                        onValueChange = {
                                            if (it.isValidNumber() && it.length <= 5)
                                                viewModel.updateReps(index, it, pos)
                                        },
                                        placeholderText = "-",
                                        modifier = Modifier.weight(1f)
                                    )

                                    Spacer(Modifier.weight(0.3f))

                                    CustomSwitch(
                                        checked = sets[pos],
                                        onCheckedChange = {
                                            if (it) {
                                                viewModel.setTotalTime(
                                                    getCurrentTimerInSeconds(
                                                        exercise.reps
                                                    )
                                                )
                                                viewModel.startTimer()
                                            }

                                            if (reps[pos].isBlank()) {
                                                viewModel.updateReps(
                                                    index,
                                                    exercise.reps?.second.toString(),
                                                    pos
                                                )
                                            }

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
            }
        )
    }
}