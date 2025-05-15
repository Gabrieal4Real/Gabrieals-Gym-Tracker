package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.cant_decide
import org.gabrieal.gymtracker.data.SelectedExercise
import org.gabrieal.gymtracker.navigation.AppNavigator
import org.gabrieal.gymtracker.ui.widgets.AnimatedDividerWithScale
import org.gabrieal.gymtracker.ui.widgets.AnimatedImage
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.BiggerText
import org.gabrieal.gymtracker.ui.widgets.ConfirmButton
import org.gabrieal.gymtracker.ui.widgets.CustomCard
import org.gabrieal.gymtracker.ui.widgets.CustomTextField
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.IncrementDecrementButton
import org.gabrieal.gymtracker.ui.widgets.RepRangePicker
import org.gabrieal.gymtracker.ui.widgets.TinyItalicText
import org.gabrieal.gymtracker.ui.widgets.TinyText
import org.gabrieal.gymtracker.ui.widgets.popOut
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.planTitles
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.repRanges
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.systemUtil.ShowToast
import org.gabrieal.gymtracker.viewmodel.EditPlanViewModel

object EditPlanScreen : Screen {
    // Create a single instance of the ViewModel
    private val viewModel = EditPlanViewModel()
    
    // Stores the selected day and exercises for editing
    private var selectedDay: String = ""
    private var callback: ((List<SelectedExercise>) -> Unit)? = null
    private var exerciseList: List<SelectedExercise> = emptyList()

    // Set callback to receive edited exercise list
    fun setCallback(onMessageSent: (List<SelectedExercise>) -> Unit) {
        callback = onMessageSent
        viewModel.setCallback(onMessageSent)
    }

    // Set the day being edited
    fun setSelectedDay(day: String) {
        selectedDay = day
        viewModel.setDay(selectedDay)
    }

    // Set the exercises for the day
    fun setExercises(exercises: List<SelectedExercise>?) {
        exerciseList = exercises ?: List(3) {
            SelectedExercise(
                name = "",
                reps = repRanges.random(),
                sets = 3
            )
        }.toMutableList()
        viewModel.setExercises(exerciseList)
        viewModel.initializeDefaultExerciseList()
    }

    @Composable
    override fun Content() {
        // Collect the UI state from the ViewModel
        val uiState by viewModel.uiState.collectAsState()
        
        // Extract state values for easier access
        val day = uiState.day
        val exercises = uiState.exercises
        val defaultExerciseList = uiState.defaultExerciseList
        val showImage = uiState.showImage
        val showRemoveDialog = uiState.showRemoveDialog
        val currentClickedPosition = uiState.currentClickedPosition

        // Main layout
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val planTitle = planTitles.find { day.contains(it) }
            BackButtonRow("Edit Plan")
            Box {
                // Main editing area
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Colors.LighterBackground)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxHeight(0.9f)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        // Header
                        TinyText(
                            "Let's start editing your",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        BiggerText(
                            "$planTitle Day",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .scale(popOut().value)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        AnimatedDividerWithScale()
                        Spacer(modifier = Modifier.height(16.dp))

                        // List of exercises
                        LazyColumn(horizontalAlignment = Alignment.CenterHorizontally) {
                            items(exercises.size) { position ->
                                CustomCard(
                                    enabled = true,
                                    content = {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        // Exercise name input
                                        TinyItalicText("Name your exercise")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        CustomTextField(
                                            value = exercises[position].name ?: "",
                                            onValueChange = { newName ->
                                                viewModel.updateExerciseName(position, newName)
                                            },
                                            placeholderText = if (defaultExerciseList.isNotEmpty() && position < defaultExerciseList.size) 
                                                defaultExerciseList[position] else "",
                                            resource = Icons.Rounded.Search to {
                                                viewModel.navigateToViewAllWorkouts(position)
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Sets input
                                        TinyItalicText("How many sets per exercise?")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        viewModel.updateExerciseSets(position, IncrementDecrementButton(
                                            exercises[position].sets ?: 3,
                                            1,
                                            20
                                        ))
                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Reps input
                                        TinyItalicText("How many reps per set?")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        RepRangePicker(
                                            ranges = repRanges,
                                            selectedRange = exercises[position].reps ?: repRanges.random(),
                                            onRangeSelected = { newReps ->
                                                viewModel.updateExerciseReps(position, newReps)
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Remove exercise button
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .clickable {
                                                    viewModel.setCurrentClickedPosition(position)
                                                    viewModel.setShowRemoveDialog(true)
                                                }
                                                .background(
                                                    color = Colors.Red,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            DescriptionText("Remove", color = Colors.White)
                                        }
                                    }
                                })
                            }
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DescriptionText(
                                    "Add more exercises to your plan +",
                                    modifier = Modifier.clickable {
                                        viewModel.addExercise()
                                    },
                                    color = Colors.LinkBlue
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }

                    // Save button
                    ConfirmButton(
                        "Save",
                        onClick = {
                            viewModel.navigateBack()
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                // Animated Image
                val updatedShowImage = AnimatedImage(showImage, Res.drawable.cant_decide, true)
                // Update the showImage state in the ViewModel
                viewModel.setShowImage(updatedShowImage)

                // Remove exercise dialog
                if (showRemoveDialog) {
                    if (exercises.size == 1) {
                        ShowToast("Need to have at least 1 workout")
                        viewModel.setShowRemoveDialog(false)
                    } else if (exercises[currentClickedPosition].name?.isBlank() == true){
                        viewModel.removeExercise(currentClickedPosition)
                        viewModel.setShowRemoveDialog(false)
                    } else {
                        ShowAlertDialog(
                            titleMessage = Pair(
                                "Remove Exercise",
                                "Are you sure you want to remove this exercise?"
                            ),
                            positiveButton = Pair("Remove") {
                                viewModel.removeExercise(currentClickedPosition)
                            },
                            negativeButton = Pair("Cancel") {
                                viewModel.setShowRemoveDialog(false)
                            }
                        )
                    }
                }
            }
        }
    }
}