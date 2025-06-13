package org.gabrieal.gymtracker.features.startWorkout.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.icon_reps
import gymtracker.composeapp.generated.resources.icon_sets
import gymtracker.composeapp.generated.resources.icon_timer
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.currentlyActiveRoutine
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutViewModel
import org.gabrieal.gymtracker.util.app.formatRestTime
import org.gabrieal.gymtracker.util.app.getCurrentTimerInSeconds
import org.gabrieal.gymtracker.util.app.getPlanTitle
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.widgets.AnimatedDividerWithScale
import org.gabrieal.gymtracker.util.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.widgets.BiggerText
import org.gabrieal.gymtracker.util.widgets.ConfirmButton
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.popOut
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

object StartWorkoutScreen : Screen, KoinComponent {
    private val viewModel: StartWorkoutViewModel by inject()

    fun setSelectedExerciseList(selectedExerciseList: SelectedExerciseList) {
        viewModel.setSelectedExerciseList(selectedExerciseList)
    }

    fun setCallback(selectedExerciseList: (SelectedExerciseList) -> Unit) {
        viewModel.setCallback(selectedExerciseList)
    }


    @OptIn(ExperimentalTime::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val selectedExerciseList = uiState.selectedExerciseList
        val currentActiveExercise = uiState.currentActiveExercise
        val showWarningReplace = uiState.showWarningReplace

        LaunchedEffect(Unit) {
            viewModel.updateCurrentActiveExercise(currentlyActiveRoutine?.first)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow("Start Workout")
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.lighterBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(colors.lighterBackground)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.92f)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        TinyText(
                            "Let's start your",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        BiggerText(
                            "${getPlanTitle(selectedExerciseList?.routineName)} Day",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .scale(popOut().value)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedDividerWithScale()
                        Spacer(modifier = Modifier.height(16.dp))
                        TinyText(
                            "Estimated workout time: ${calculateEstimatedTime(selectedExerciseList)}",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            content = {
                                items(
                                    selectedExerciseList?.exercises?.size ?: 0
                                ) { selectedExercise ->
                                    ExerciseCard(selectedExerciseList, selectedExercise)
                                }
                            }
                        )
                    }
                }

                val title =
                    when (currentActiveExercise) {
                        null -> "Start Workout"
                        selectedExerciseList -> "Complete Workout"
                        else -> "Replace Workout"
                    }

                ConfirmButton(
                    title,
                    onClick = {
                        when (currentActiveExercise) {
                            null -> {
                                viewModel.startWorkout(selectedExerciseList)
                                AppNavigator.navigateBack()
                            }

                            selectedExerciseList -> {
                                viewModel.markWorkoutAsDone()
                                viewModel.startWorkout(null)
                            }

                            else -> {
                                viewModel.setShowWarningReplace(true)
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp)
                )
            }

            if (showWarningReplace) {
                ShowAlertDialog(
                    titleMessage = Pair(
                        "Replace Workout",
                        "Are you sure you want to replace your current workout with this one?"
                    ),
                    positiveButton = Pair("Proceed") {
                        viewModel.setShowWarningReplace(false)
                        viewModel.startWorkout(selectedExerciseList)
                        AppNavigator.navigateBack()
                    },
                    negativeButton = Pair("Cancel") {
                        viewModel.setShowWarningReplace(false)
                    }
                )
            }
        }
    }

    @Composable
    private fun ExerciseCard(selectedExerciseList: SelectedExerciseList?, selectedExercise: Int) {
        CustomCard(
            enabled = true,
            content = {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    SubtitleText(
                        selectedExerciseList?.exercises?.get(selectedExercise)?.name.orEmpty()
                    )
                    val exercise = selectedExerciseList?.exercises?.getOrNull(selectedExercise)
                    val sets = Pair("${exercise?.sets ?: "-"} sets", Res.drawable.icon_sets)
                    val reps = Pair(
                        "${exercise?.reps?.first ?: "-"} to ${exercise?.reps?.second ?: "-"} reps",
                        Res.drawable.icon_reps
                    )
                    val timer = Pair(
                        "${formatRestTime(getCurrentTimerInSeconds(exercise?.reps))} rest",
                        Res.drawable.icon_timer
                    )
                    val listOfPairs = listOf(sets, reps, timer)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        listOfPairs.forEachIndexed { index, pair ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.weight(1f)
                            ) {
                                Image(
                                    painter = painterResource(pair.second),
                                    contentDescription = pair.first,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier.height(32.dp),
                                    colorFilter = ColorFilter.tint(colors.textPrimary)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                TinyText(pair.first)
                            }
                            if (index != listOfPairs.lastIndex) {
                                Spacer(modifier = Modifier.width(8.dp))
                                DashedDivider()
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                }
            }
        )
    }

    private fun calculateEstimatedTime(selectedExerciseList: SelectedExerciseList?): String {
        val estimatedTime = selectedExerciseList?.exercises?.sumOf {
            (getCurrentTimerInSeconds(it.reps) + 60) * (it.sets ?: 1)
        } ?: 0
        return formatRestTime(estimatedTime)
    }
}