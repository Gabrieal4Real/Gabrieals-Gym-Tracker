package org.gabrieal.gymtracker.features.editPlan.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.cant_decide
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.features.editPlan.viewmodel.EditPlanViewModel
import org.gabrieal.gymtracker.util.app.getPlanTitle
import org.gabrieal.gymtracker.util.app.planTitles
import org.gabrieal.gymtracker.util.app.repRanges
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.systemUtil.ShowToast
import org.gabrieal.gymtracker.util.widgets.AnimatedDividerWithScale
import org.gabrieal.gymtracker.util.widgets.AnimatedImage
import org.gabrieal.gymtracker.util.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.widgets.BiggerText
import org.gabrieal.gymtracker.util.widgets.ConfirmButton
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.CustomTextField
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.IncrementDecrementButton
import org.gabrieal.gymtracker.util.widgets.RepRangePicker
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.popOut
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object EditPlanScreen : Screen, KoinComponent {
    private val viewModel: EditPlanViewModel by inject()

    fun setCallback(onMessageSent: (List<SelectedExercise>) -> Unit) {
        viewModel.setCallback(onMessageSent)
    }

    fun setSelectedDay(day: String) {
        viewModel.setDay(day)
    }

    fun setExercises(exercises: List<SelectedExercise>?) {
        val exerciseList = exercises ?: List(3) {
            SelectedExercise(
                name = "",
                reps = repRanges.random(),
                sets = 3
            )
        }.toMutableList()
        viewModel.setExercises(exerciseList)
        viewModel.initializePlaceholderList()
    }


    fun setEditMode(isEditMode: Boolean) {
        viewModel.setEditMode(isEditMode)
    }

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val day = uiState.day
        val exercises = uiState.exercises
        val placeholderList = uiState.placeHolderList
        val showImage = uiState.showImage
        val showRemoveDialog = uiState.showRemoveDialog
        val currentClickedPosition = uiState.currentClickedPosition
        val isEditMode = uiState.isEditMode

        // Main layout
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow("Edit Plan")
            Box {
                // Main editing area
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
                        // Header
                        TinyText(
                            "Let's start editing your",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        BiggerText(
                            "${getPlanTitle(planTitles.find { day.contains(it) })} Day",
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
                                                placeholderText = if (placeholderList.isNotEmpty() && position < placeholderList.size)
                                                    placeholderList[position] else "",
                                                resource = Icons.Rounded.Search to {
                                                    viewModel.navigateToViewAllWorkouts(position)
                                                }
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Sets input
                                            TinyItalicText("How many sets per exercise?")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            viewModel.updateExerciseSets(
                                                position, IncrementDecrementButton(
                                                    exercises[position].sets ?: 3,
                                                    1,
                                                    20
                                                )
                                            )
                                            Spacer(modifier = Modifier.height(24.dp))

                                            // Reps input
                                            TinyItalicText("How many reps per set?")
                                            Spacer(modifier = Modifier.height(8.dp))
                                            RepRangePicker(
                                                ranges = repRanges,
                                                selectedRange = exercises[position].reps
                                                    ?: repRanges.random(),
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
                                                        color = colors.deleteRed,
                                                        shape = RoundedCornerShape(8.dp)
                                                    )
                                                    .padding(horizontal = 16.dp, vertical = 8.dp)
                                            ) {
                                                DescriptionText("Remove", color = colors.white)
                                            }
                                        }
                                    })
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                            item {
                                Spacer(modifier = Modifier.height(8.dp))
                                DescriptionText(
                                    "Add more exercises to your plan +",
                                    modifier = Modifier.clickable {
                                        viewModel.addExercise()
                                    },
                                    color = colors.linkBlue
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }

                // Save button
                ConfirmButton(
                    "Save",
                    onClick = {
                        viewModel.navigateBack()
                    },
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp),
                )

                // Animated Image
                if (!isEditMode) {
                    val updatedShowImage = AnimatedImage(showImage, Res.drawable.cant_decide, true)
                    viewModel.setShowImage(updatedShowImage)
                }

                // Remove exercise dialog
                if (showRemoveDialog) {
                    if (exercises.size == 1) {
                        ShowToast("Need to have at least 1 workout")
                        viewModel.setShowRemoveDialog(false)
                    } else if (exercises[currentClickedPosition].name?.isBlank() == true) {
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