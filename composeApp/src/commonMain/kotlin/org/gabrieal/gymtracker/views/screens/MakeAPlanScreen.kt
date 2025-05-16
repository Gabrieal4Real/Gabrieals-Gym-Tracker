package org.gabrieal.gymtracker.views.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.start_editing
import org.gabrieal.gymtracker.views.widgets.AnimatedImage
import org.gabrieal.gymtracker.views.widgets.BackButtonRow
import org.gabrieal.gymtracker.views.widgets.ConfirmButton
import org.gabrieal.gymtracker.views.widgets.CustomCard
import org.gabrieal.gymtracker.views.widgets.DescriptionText
import org.gabrieal.gymtracker.views.widgets.SubtitleText
import org.gabrieal.gymtracker.views.widgets.TinyItalicText
import org.gabrieal.gymtracker.views.widgets.TinyText
import org.gabrieal.gymtracker.util.appUtil.Colors
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.fullDays
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.getCurrentPlan
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.viewmodel.MakeAPlanViewModel

object MakeAPlanScreen : Screen {
    private val viewModel = MakeAPlanViewModel()

    fun setSelectedDay(selectedDays: List<Boolean>) {
        viewModel.setSelectedDays(selectedDays)
    }

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val selectedDays = uiState.selectedDays
        val selectedRoutineList = uiState.selectedRoutineList
        var showImage = uiState.showImage
        val showWarningBack = uiState.showWarningBack
        val saveRoutineList = uiState.saveRoutineList

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BackButtonRow(Resources.strings.makeAPlan) {
                viewModel.setShowWarningBack(true)
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Colors.LighterBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    SubtitleText(
                        Resources.strings.xDayWorkoutxDayRest(
                            selectedDays.count { it },
                            selectedDays.count { !it }
                        ),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // Day Cards
                    fullDays.forEachIndexed { index, day ->
                        val isActive = selectedDays[index]
                        val hasExercises = selectedRoutineList.any { it.day == day }
                        CustomCard(
                            enabled = isActive,
                            onClick = {
                                if (isActive) {
                                    viewModel.navigateToEditPlan(index, day)
                                }
                            },
                            content = {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column {
                                        DescriptionText(day)
                                        if (isActive) {
                                            TinyItalicText(getCurrentPlan(selectedDays)[index], color = Colors.TextSecondary)
                                            TinyText(if (hasExercises) Resources.strings.edited else Resources.strings.notEditedYet)
                                        }
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    if (!isActive) {
                                        TinyItalicText(Resources.strings.restDay)
                                    } else {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                            contentDescription = null,
                                            tint = Colors.TextPrimary
                                        )
                                    }
                                }
                            }
                        })
                    }
                }

                // Confirm Button
                if (viewModel.areAllActiveDaysEdited()) {
                    ConfirmButton(
                        "Let's Pump It Up",
                        onClick = {
                            viewModel.setSaveRoutineList(true)
                        },
                        modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                    )
                }

                // Animated Image
                showImage = AnimatedImage(showImage, Res.drawable.start_editing, true)
                // Update the showImage state in the ViewModel
                viewModel.setShowImage(showImage)

                if (showWarningBack) {
                    ShowAlertDialog(
                        titleMessage = Pair(
                            "Are you sure?",
                            "You will lose all previous changes"
                        ),
                        positiveButton = Pair("Proceed") {
                            viewModel.setShowWarningBack(false)
                            viewModel.navigateBack()
                        },
                        negativeButton = Pair("Cancel") {
                            viewModel.setShowWarningBack(false)
                        }
                    )
                }

                // Process save routine list
                if (saveRoutineList) {
                    viewModel.saveRoutineList()
                }
            }
        }
    }
}
