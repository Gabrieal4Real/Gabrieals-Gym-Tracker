package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.tier_0
import gymtracker.composeapp.generated.resources.tier_1
import gymtracker.composeapp.generated.resources.tier_2
import gymtracker.composeapp.generated.resources.tier_3
import gymtracker.composeapp.generated.resources.youtube
import org.gabrieal.gymtracker.data.Routine
import org.gabrieal.gymtracker.ui.allExistingExerciseList
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.BiggerText
import org.gabrieal.gymtracker.ui.widgets.CustomTextField
import org.gabrieal.gymtracker.ui.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.DropDownFilter
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.ui.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.appUtil.ExtraBoldText
import org.gabrieal.gymtracker.util.systemUtil.OpenURL
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.jetbrains.compose.resources.painterResource

object ViewAllWorkoutScreen : Screen {
    private var callback: ((String) -> Unit)? = null

    fun setCallback(onMessageSent: (String) -> Unit) {
        callback = onMessageSent
    }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val allMuscleGroups = Routine.MuscleGroup.entries.map { it.displayName }

        var searchFilter by remember { mutableStateOf("") }
        var selectedFilters by remember { mutableStateOf(setOf<String>()) }
        var selectedWorkout by remember { mutableStateOf("") }

        var allWorkouts by remember { mutableStateOf(allExistingExerciseList) }
        var isClicked by remember { mutableStateOf(false) }
        var currentUrl by remember { mutableStateOf("") }

        var showConfirmAddToRoutineDialog by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow("All Workouts")
            Box(
                modifier = Modifier.fillMaxSize().background(Colors.LighterBackground)
                    .padding(16.dp)
            ) {
                DropDownFilter(
                    filterOptions = allMuscleGroups,
                    selectedFilters = selectedFilters,
                    onFilterSelected = { filter ->
                        selectedFilters = if (filter in selectedFilters) {
                            selectedFilters - filter
                        } else {
                            selectedFilters + filter
                        }

                        allWorkouts = allExistingExerciseList.filter { it.name.contains(searchFilter, ignoreCase = true) && it.muscleGroup.containsAll(selectedFilters) }.toMutableList()
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                )
                Column(modifier = Modifier.fillMaxSize()) {
                    SubtitleText("All Workouts")
                    DescriptionItalicText("Select muscle groups to filter by")
                    Spacer(modifier = Modifier.padding(4.dp))
                    CustomTextField(
                        value = searchFilter,
                        onValueChange = { search ->
                            allWorkouts = allExistingExerciseList.filter { it.name.contains(search, ignoreCase = true) && it.muscleGroup.containsAll(selectedFilters) }.toMutableList()
                            searchFilter = search
                        },
                        placeholderText = allExistingExerciseList.random().name
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    LazyColumn {
                        items(allWorkouts.size) {
                            Card(
                                shape = RoundedCornerShape(8.dp),
                                backgroundColor = Colors.CardBackground,
                                border = BorderStroke(2.dp, Colors.BorderStroke),
                                modifier = Modifier.padding(bottom = 8.dp).fillMaxSize().clickable(
                                    onClick = {
                                        showConfirmAddToRoutineDialog = true
                                        selectedWorkout = allWorkouts[it].name
                                    },
                                )
                            ) {
                                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f).padding(16.dp)) {
                                        DescriptionText(allWorkouts[it].name)
                                        TinyItalicText(allWorkouts[it].muscleGroup.joinToString(", "), color = Colors.TextSecondary)
                                        Image(
                                            painter = painterResource(Res.drawable.youtube),
                                            contentDescription = "Youtube",
                                            modifier = Modifier.size(24.dp).padding(top = 8.dp).clickable {
                                                currentUrl = "https://www.youtube.com/results?search_query=how+to+do+${allWorkouts[it].name}"
                                                isClicked = true
                                            },
                                        )

                                    }
                                    TierImage(allWorkouts[it].tier)

                                    if (isClicked) {
                                        isClicked = false
                                        OpenURL(currentUrl)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (showConfirmAddToRoutineDialog) {
                ShowAlertDialog(
                    titleMessage = Pair(
                        "Add Workout?",
                        "Do you want to add $selectedWorkout to your routine?"
                    ),
                    positiveButton = Pair("Proceed") {
                        callback?.invoke(selectedWorkout)
                        navigator.pop()
                        showConfirmAddToRoutineDialog = false
                    },
                    negativeButton = Pair("Cancel") {
                        showConfirmAddToRoutineDialog = false
                    }
                )
            }
        }
    }

    @Composable
    fun TierImage(tier: Int) {
        val drawableResource = when (tier) {
            0 -> Res.drawable.tier_0
            1 -> Res.drawable.tier_1
            2 -> Res.drawable.tier_2
            3 -> Res.drawable.tier_3
            else -> Res.drawable.tier_3
        }

        Image(
            painter = painterResource(drawableResource),
            contentDescription = drawableResource.toString(),
            modifier = Modifier.size(100.dp).padding(end = 8.dp),
        )
    }
}