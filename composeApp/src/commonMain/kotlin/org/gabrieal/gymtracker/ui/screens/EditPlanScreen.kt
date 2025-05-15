package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.cant_decide
import org.gabrieal.gymtracker.data.SelectedExercise
import org.gabrieal.gymtracker.ui.allExistingExerciseList
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

object EditPlanScreen : Screen {
    // Stores the selected day and exercises for editing
    private var selectedDay: String = ""
    private var callback: ((MutableList<SelectedExercise>) -> Unit)? = null
    private var exerciseList: MutableList<SelectedExercise> = mutableListOf()

    // Set callback to receive edited exercise list
    fun setCallback(onMessageSent: (MutableList<SelectedExercise>) -> Unit) {
        callback = onMessageSent
    }

    // Set the day being edited
    fun setSelectedDay(day: String) {
        selectedDay = day
    }

    // Set the exercises for the day, or initialize with 3 blank exercises
    fun setExercises(exercises: List<SelectedExercise>?) {
        exerciseList = exercises?.toMutableList() ?: List(3) {
            SelectedExercise(
                name = "",
                reps = repRanges.random(),
                sets = 3
            )
        }.toMutableList()
    }

    @Composable
    override fun Content() {
        // Compose state
        var showImage = true
        val navigator = LocalNavigator.currentOrThrow

        // List sizes and state for exercises
        var defaultListSize by rememberSaveable { mutableStateOf(exerciseList.size) }
        var defaultExerciseList by rememberSaveable { mutableStateOf(List(defaultListSize) { "" }) }
        var selectedExercises by rememberSaveable { mutableStateOf(exerciseList.toList()) }
        var showRemoveDialog by rememberSaveable { mutableStateOf(false) }
        var currentClickedPosition by rememberSaveable { mutableStateOf(0) }

        // Randomize default exercise names for placeholders
        LaunchedEffect(Unit) {
            repeat(defaultListSize) { position ->
                defaultExerciseList = defaultExerciseList.toMutableList().apply {
                    this[position] = allExistingExerciseList.random().name
                }
            }
        }

        // Main layout
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val planTitle = planTitles.find { selectedDay.contains(it) }
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
                            items(defaultListSize) { position ->
                                CustomCard(
                                    enabled = true,
                                    content = {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        // Exercise name input
                                        TinyItalicText("Name your exercise")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        CustomTextField(
                                            value = selectedExercises[position].name ?: "",
                                            onValueChange = { newName ->
                                                selectedExercises = selectedExercises.toMutableList().apply {
                                                    this[position] = this[position].copy(name = newName)
                                                }
                                            },
                                            placeholderText = defaultExerciseList[position],
                                            resource = Icons.Rounded.Search to {
                                                ViewAllWorkoutScreen.setCallback { exerciseName ->
                                                    selectedExercises = selectedExercises.toMutableList().apply {
                                                        this[position] = this[position].copy(name = exerciseName)
                                                    }
                                                }
                                                navigator.push(ViewAllWorkoutScreen)
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Sets input
                                        TinyItalicText("How many sets per exercise?")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        selectedExercises = selectedExercises.toMutableList().apply {
                                            this[position] = this[position].copy(
                                                sets = IncrementDecrementButton(
                                                    selectedExercises[position].sets ?: 3,
                                                    1,
                                                    20
                                                )
                                            )
                                        }
                                        Spacer(modifier = Modifier.height(24.dp))

                                        // Reps input
                                        TinyItalicText("How many reps per set?")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        RepRangePicker(
                                            ranges = repRanges,
                                            selectedRange = selectedExercises[position].reps ?: repRanges.random(),
                                            onRangeSelected = { newReps ->
                                                selectedExercises = selectedExercises.toMutableList().apply {
                                                    this[position] = this[position].copy(reps = newReps)
                                                }
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Remove exercise button
                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.CenterHorizontally)
                                                .clickable {
                                                    showRemoveDialog = true
                                                    currentClickedPosition = position
                                                }
                                                .background(
                                                    color = Colors.Red,
                                                    shape = RoundedCornerShape(8.dp)
                                                )
                                                .padding(
                                                    start = 8.dp,
                                                    end = 8.dp,
                                                    top = 4.dp,
                                                    bottom = 4.dp
                                                )
                                        ) {
                                            DescriptionText("Remove")
                                        }
                                    }
                                })

                                // Add more exercises control at the end of the list
                                if (position == defaultListSize - 1) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DescriptionText(
                                        "Add more exercises to your plan +",
                                        modifier = Modifier.clickable {
                                            defaultListSize++
                                            defaultExerciseList = defaultExerciseList.toMutableList().apply {
                                                this.add(allExistingExerciseList.random().name)
                                            }
                                            selectedExercises = selectedExercises.toMutableList().apply {
                                                this.add(SelectedExercise(
                                                    name = "",
                                                    reps = repRanges.random(),
                                                    sets = 3
                                                ))
                                            }
                                        },
                                        color = Colors.LinkBlue
                                    )
                                }
                            }
                        }
                    }
                }

                // Confirm button at the bottom right
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    ConfirmButton(
                        "Confirm $planTitle Day",
                        onClick = {
                            val tempSelectedExerciseList = selectedExercises.filter { it.name?.isNotBlank() == true }.toMutableList()
                            callback?.invoke(tempSelectedExerciseList)
                            navigator.pop()
                        },
                        enabled = selectedExercises.any { it.name?.isNotBlank() == true }
                    )
                }
                // Animated image for visual feedback
                showImage = AnimatedImage(showImage, Res.drawable.cant_decide, true)
            }

            // Remove exercise dialog
            if (showRemoveDialog) {
                if (defaultListSize > 1 && selectedExercises[currentClickedPosition].name?.isNotBlank() == true) {
                    ShowAlertDialog(
                        titleMessage = Pair(
                            "Remove exercise?",
                            "Are you sure you want to remove this exercise?"
                        ),
                        positiveButton = Pair("Proceed") {
                            showRemoveDialog = false
                            defaultListSize--
                            defaultExerciseList = defaultExerciseList.toMutableList().apply {
                                this.removeAt(currentClickedPosition)
                            }
                            selectedExercises = selectedExercises.toMutableList().apply {
                                this.removeAt(currentClickedPosition)
                            }
                        },
                        negativeButton = Pair("Cancel") {
                            showRemoveDialog = false
                        }
                    )
                } else if (defaultListSize > 1) {
                    showRemoveDialog = false
                    defaultListSize--
                    defaultExerciseList = defaultExerciseList.toMutableList().apply {
                        this.removeAt(currentClickedPosition)
                    }
                    selectedExercises = selectedExercises.toMutableList().apply {
                        this.removeAt(currentClickedPosition)
                    }
                } else {
                    ShowToast("Need to have at least 1 workout")
                    showRemoveDialog = false
                }
            }
        }
    }
}