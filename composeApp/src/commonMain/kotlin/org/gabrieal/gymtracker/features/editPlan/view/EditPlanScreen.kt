package org.gabrieal.gymtracker.features.editPlan.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DismissDirection
import androidx.compose.material.DismissValue
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.rememberDismissState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
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

    fun setCallback(onMessageSent: (List<SelectedExercise>) -> Unit) =
        viewModel.setCallback(onMessageSent)

    fun setSelectedDay(day: String) = viewModel.setDay(day)

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


    fun setEditMode(isEditMode: Boolean) = viewModel.setEditMode(isEditMode)

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

        if (!isEditMode) {
            val updatedShowImage = AnimatedImage(showImage, Res.drawable.cant_decide, true)
            viewModel.setShowImage(updatedShowImage)
        }

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

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow("Edit Plan")
            Box {
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
                        Header(Modifier.align(Alignment.CenterHorizontally), day)

                        LazyColumn(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(bottom = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(exercises.size) { position ->
                                ExerciseCard(
                                    exercise = exercises[position],
                                    placeholder = placeholderList[position],
                                    position = position,
                                    modifier = Modifier.animateItem()
                                )
                            }

                            item {
                                DescriptionText(
                                    "Add more exercises to your plan +",
                                    modifier = Modifier.clickable { viewModel.addExercise() },
                                    color = colors.linkBlue
                                )
                            }
                        }
                    }
                }

                ConfirmButton(
                    "Save",
                    onClick = {
                        viewModel.navigateBack()
                    },
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter).padding(16.dp),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ExerciseCard(
        exercise: SelectedExercise,
        placeholder: String,
        position: Int,
        modifier: Modifier
    ) {
        val dismissState = rememberDismissState(
            confirmStateChange = {
                if (it == DismissValue.DismissedToStart) {
                    viewModel.setCurrentClickedPosition(position)
                    viewModel.setShowRemoveDialog(true)
                }
                false
            }
        )

        SwipeToDismiss(
            modifier = modifier,
            state = dismissState,
            directions = setOf(DismissDirection.EndToStart),
            background = {
                val color = when (dismissState.dismissDirection) {
                    DismissDirection.EndToStart -> colors.deleteRed
                    DismissDirection.StartToEnd -> Color.Transparent
                    null -> Color.Transparent
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp))
                        .background(color)
                        .padding(24.dp),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Delete,
                        contentDescription = "Delete",
                        tint = colors.white,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
            },
            dismissContent = {
                CustomCard(
                    enabled = true,
                    content = {
                        Column(modifier = Modifier.padding(16.dp)) {
                            TinyItalicText("Name of exercise")
                            Spacer(modifier = Modifier.height(8.dp))
                            CustomTextField(
                                value = exercise.name ?: "",
                                onValueChange = { newName ->
                                    viewModel.updateExerciseName(position, newName)
                                },
                                placeholderText = placeholder,
                                resource = Icons.Rounded.Search to {
                                    viewModel.navigateToViewAllWorkouts(position)
                                }
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                                Column {
                                    TinyItalicText("No. of Sets")
                                    Spacer(modifier = Modifier.height(8.dp))
                                    viewModel.updateExerciseSets(
                                        position, IncrementDecrementButton(
                                            exercise.sets ?: 3,
                                            1,
                                            20
                                        )
                                    )
                                }

                                Spacer(modifier = Modifier.width(16.dp))

                                Column {
                                    TinyItalicText("No. of Reps")
                                    RepRangePicker(
                                        ranges = repRanges,
                                        selectedRange = exercise.reps
                                            ?: repRanges.random(),
                                        onRangeSelected = { newReps ->
                                            viewModel.updateExerciseReps(position, newReps)
                                        }
                                    )
                                }
                            }
                        }
                    })
            })

    }

    @Composable
    fun Header(modifier: Modifier, day: String) {
        TinyText(
            "Let's start editing your",
            modifier = modifier
        )
        BiggerText(
            "${getPlanTitle(planTitles.find { day.contains(it) })} Day",
            modifier = modifier.scale(popOut().value)
        )
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedDividerWithScale()
        Spacer(modifier = Modifier.height(16.dp))
    }
}