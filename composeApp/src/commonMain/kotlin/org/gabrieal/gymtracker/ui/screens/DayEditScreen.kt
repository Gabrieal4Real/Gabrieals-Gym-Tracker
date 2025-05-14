package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.cant_decide
import org.gabrieal.gymtracker.data.SelectedExercise
import org.gabrieal.gymtracker.ui.allExistingExerciseList
import org.gabrieal.gymtracker.ui.widgets.AnimatedImage
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.BiggerText
import org.gabrieal.gymtracker.ui.widgets.ConfirmButton
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

object DayEditScreen : Screen {
    private var selectedDay: String = ""
    private var callback: ((MutableList<SelectedExercise>) -> Unit)? = null
    private var exerciseList: MutableList<SelectedExercise> = mutableListOf()

    fun setCallback(onMessageSent: (MutableList<SelectedExercise>) -> Unit) {
        callback = onMessageSent
    }

    fun setSelectedDay(day: String) {
        selectedDay = day
    }

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
        var showImage = true
        val navigator = LocalNavigator.currentOrThrow

        var defaultListSize by rememberSaveable { mutableStateOf(exerciseList.size) }
        var defaultExerciseList by rememberSaveable { mutableStateOf(List(defaultListSize) { "" }) }

        var selectedExerciseList by rememberSaveable { mutableStateOf(exerciseList.mapNotNull { it.name })}
        var selectedExerciseSetList by rememberSaveable { mutableStateOf(exerciseList.mapNotNull { it.sets })}
        var selectedExerciseRepRangeList by rememberSaveable { mutableStateOf(exerciseList.mapNotNull { it.reps })}

        var showRemoveDialog by rememberSaveable { mutableStateOf(false) }
        var currentClickedPosition by rememberSaveable { mutableStateOf(0) }

        LaunchedEffect(Unit) {
            repeat(defaultListSize) { position ->
                defaultExerciseList = defaultExerciseList.toMutableList().apply {
                    this[position] = allExistingExerciseList.random().name
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val selectedDay = planTitles.find { selectedDay.contains(it) }
            BackButtonRow("Edit Plan")
            Box {
                Column(
                    modifier = Modifier.fillMaxSize().background(Colors.LighterBackground)
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.fillMaxHeight(0.9f).align(Alignment.CenterHorizontally)) {
                        TinyText(
                            "Let's start editing your",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        BiggerText(
                            "$selectedDay Day",
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                .scale(popOut().value)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier.background(Colors.BorderStroke).fillMaxWidth(0.8f)
                                .height(2.dp).align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        LazyColumn (horizontalAlignment = Alignment.CenterHorizontally) {
                            items(defaultListSize) { position ->
                                Card(
                                    shape = RoundedCornerShape(8.dp),
                                    backgroundColor = Colors.CardBackground,
                                    border = BorderStroke(2.dp, Colors.BorderStroke),
                                    modifier = Modifier.padding(bottom = 8.dp).fillMaxSize()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        TinyItalicText("Name your exercise")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        CustomTextField(
                                            value = selectedExerciseList[position],
                                            onValueChange = {
                                                selectedExerciseList =
                                                    selectedExerciseList.toMutableList()
                                                        .apply { this[position] = it }
                                            },
                                            placeholderText = defaultExerciseList[position],
                                            resource = Icons.Rounded.Search to {
                                                ViewAllWorkoutScreen.setCallback {
                                                    selectedExerciseList =
                                                        selectedExerciseList.toMutableList()
                                                            .apply { this[position] = it }
                                                }

                                                navigator.push(ViewAllWorkoutScreen)
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(24.dp))
                                        TinyItalicText("How many sets per exercise?")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        selectedExerciseSetList =
                                            selectedExerciseSetList.toMutableList()
                                                .apply {
                                                    this[position] = IncrementDecrementButton(
                                                        selectedExerciseSetList[position],
                                                        1,
                                                        20
                                                    )
                                                }
                                        Spacer(modifier = Modifier.height(24.dp))
                                        TinyItalicText("How many reps per set?")
                                        Spacer(modifier = Modifier.height(8.dp))
                                        RepRangePicker(
                                            ranges = repRanges,
                                            selectedRange = selectedExerciseRepRangeList[position],
                                            onRangeSelected = {
                                                selectedExerciseRepRangeList =
                                                    selectedExerciseRepRangeList.toMutableList()
                                                        .apply { this[position] = it }
                                            }
                                        )
                                        Spacer(modifier = Modifier.height(16.dp))
                                        Box(
                                            modifier = Modifier.align(Alignment.CenterHorizontally)
                                                .clickable {
                                                    showRemoveDialog = true
                                                    currentClickedPosition = position
                                                }.background(
                                                    color = Colors.Red,
                                                    shape = RoundedCornerShape(8.dp)
                                                ).padding(
                                                    start = 8.dp,
                                                    end = 8.dp,
                                                    top = 4.dp,
                                                    bottom = 4.dp
                                                )
                                        ) {
                                            DescriptionText("Remove")
                                        }
                                    }
                                }
                                if (position == defaultListSize - 1) {
                                    Spacer(modifier = Modifier.height(8.dp))
                                    DescriptionText(
                                        "Add more exercises to your plan +",
                                        modifier = Modifier.clickable {
                                                defaultListSize++
                                                defaultExerciseList =
                                                    defaultExerciseList.toMutableList().apply {
                                                        this.add(allExistingExerciseList.random().name)
                                                    }
                                                selectedExerciseList =
                                                    selectedExerciseList.toMutableList().apply {
                                                        this.add("")
                                                    }
                                                selectedExerciseSetList =
                                                    selectedExerciseSetList.toMutableList().apply {
                                                        this.add(3)
                                                    }
                                                selectedExerciseRepRangeList =
                                                    selectedExerciseRepRangeList.toMutableList().apply {
                                                        this.add(repRanges.random())
                                                    }
                                            },
                                        color = Colors.LinkBlue
                                    )
                                }
                            }
                        }
                    }
                }
                Box(
                    contentAlignment = Alignment.BottomEnd,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    ConfirmButton(
                        "Confirm $selectedDay Day",
                        onClick = {
                            val tempSelectedExerciseList = mutableListOf<SelectedExercise>()

                            selectedExerciseList.forEach { editedExercise ->
                                if (editedExercise.isNotBlank()) {
                                    tempSelectedExerciseList.add(SelectedExercise(
                                        name = editedExercise,
                                        reps = selectedExerciseRepRangeList[selectedExerciseList.indexOf(editedExercise)],
                                        sets = selectedExerciseSetList[selectedExerciseList.indexOf(editedExercise)]
                                    ))
                                }
                            }

                            callback?.invoke(tempSelectedExerciseList)
                            navigator.pop()
                        },
                        enabled = selectedExerciseList.any { it.isNotBlank() }
                    )
                }
                showImage = AnimatedImage(showImage, Res.drawable.cant_decide, true)
            }

            if (showRemoveDialog) {
                if (defaultListSize > 1 && selectedExerciseList[currentClickedPosition].isNotBlank()) {
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
                            selectedExerciseList = selectedExerciseList.toMutableList().apply {
                                this.removeAt(currentClickedPosition)
                            }
                            selectedExerciseSetList = selectedExerciseSetList.toMutableList().apply {
                                this.removeAt(currentClickedPosition)
                            }
                            selectedExerciseRepRangeList =
                                selectedExerciseRepRangeList.toMutableList().apply {
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
                    selectedExerciseList = selectedExerciseList.toMutableList().apply {
                        this.removeAt(currentClickedPosition)
                    }
                    selectedExerciseSetList = selectedExerciseSetList.toMutableList().apply {
                        this.removeAt(currentClickedPosition)
                    }
                    selectedExerciseRepRangeList =
                        selectedExerciseRepRangeList.toMutableList().apply {
                            this.removeAt(currentClickedPosition)
                        }
                }  else {
                    ShowToast("Need to have at least 1 workout")
                    showRemoveDialog = false
                }
            }
        }
    }
}