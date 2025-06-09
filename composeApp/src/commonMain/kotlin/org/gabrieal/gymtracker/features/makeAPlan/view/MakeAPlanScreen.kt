package org.gabrieal.gymtracker.features.makeAPlan.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.internal.BackHandler
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.start_editing
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.features.makeAPlan.viewmodel.MakeAPlanViewModel
import org.gabrieal.gymtracker.templates
import org.gabrieal.gymtracker.util.app.getCurrentPlan
import org.gabrieal.gymtracker.util.app.longFormDays
import org.gabrieal.gymtracker.util.app.planTitles
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.systemUtil.formatInstantToDate
import org.gabrieal.gymtracker.util.systemUtil.getMondayOrSameInstant
import org.gabrieal.gymtracker.util.widgets.AnimatedImage
import org.gabrieal.gymtracker.util.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.widgets.ButtonType
import org.gabrieal.gymtracker.util.widgets.ConfirmButton
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.IconNext
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

object MakeAPlanScreen : Screen {
    private val viewModel = MakeAPlanViewModel()

    fun setSelectedDay(selectedDays: List<Boolean>) {
        viewModel.setSelectedDays(selectedDays)
    }

    fun setSelectedRoutineList(routineList: List<SelectedExerciseList>) {
        if (routineList.isEmpty()) return
        viewModel.updateSelectedRoutineList(routineList)
    }

    fun setEditMode(isEditMode: Boolean) {
        viewModel.setEditMode(isEditMode)
    }

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val selectedDays = uiState.selectedDays
        val selectedRoutineList = uiState.selectedRoutineList
        var showImage = uiState.showImage
        val showWarningBack = uiState.showWarningBack
        val showOverrideWarning = uiState.showOverrideWarning
        val isEditMode = uiState.isEditMode

        BackHandler(enabled = true) {
            viewModel.setShowWarningBack(true)
        }

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
                    .background(colors.lighterBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(if (viewModel.areAllActiveDaysEdited()) 0.8f else 0.9f)
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
                    longFormDays.forEachIndexed { index, day ->
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
                                            DescriptionText(
                                                day,
                                                color = if (isActive) colors.textPrimary else colors.white
                                            )
                                            if (isActive) {
                                                TinyItalicText(
                                                    getCurrentPlan(selectedDays)[index],
                                                    color = colors.textSecondary
                                                )
                                                TinyText(if (hasExercises) Resources.strings.edited else Resources.strings.notEditedYet)
                                            }
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (!isActive) {
                                            TinyItalicText(
                                                Resources.strings.restDay,
                                                color = colors.white
                                            )
                                        } else {
                                            IconNext()
                                        }
                                    }
                                }
                            }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Column(
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomEnd).padding(16.dp)
                ) {
                    //Template Button
                    ConfirmButton(
                        text = "Use a Template instead",
                        onClick = {
                            if (!viewModel.areAllActiveDaysEdited())
                                getTemplate(selectedDays)
                            else {
                                viewModel.setOverrideWarning(true)
                            }
                        },
                        buttonType = ButtonType.RED,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Confirm Button
                    if (viewModel.areAllActiveDaysEdited()) {
                        ConfirmButton(
                            "Let's Pump It Up",
                            onClick = {
                                viewModel.saveRoutineList()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Animated Image
                if (!isEditMode) {
                    showImage = AnimatedImage(showImage, Res.drawable.start_editing, true)
                    viewModel.setShowImage(showImage)
                }

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

                if (showOverrideWarning) {
                    ShowAlertDialog(
                        titleMessage = Pair(
                            "Are you sure?",
                            "This action will override current routine list"
                        ),
                        positiveButton = Pair("Proceed") {
                            viewModel.setOverrideWarning(false)
                            getTemplate(selectedDays)
                        },
                        negativeButton = Pair("Cancel") {
                            viewModel.setOverrideWarning(false)
                        }
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    private fun getTemplate(selectedDays: List<Boolean>) {
        val dayCount = selectedDays.count { it }

        val selectedTemplate = when (dayCount) {
            1 -> templates.`1_day`.random()
            2 -> templates.`2_day`.random()
            3 -> templates.`3_day`.random()
            4 -> templates.`4_day`.random()
            5 -> templates.`5_day`.random()
            else -> null
        }

        selectedTemplate?.let { template ->
            var fullDayIndex = 0
            selectedDays.forEachIndexed { index, isSelected ->
                if (isSelected && fullDayIndex < template.size) {
                    template[fullDayIndex].day = longFormDays[index]
                    val planTitle =
                        planTitles.find { getCurrentPlan(selectedDays)[index].contains(it) }
                    template[fullDayIndex].routineName = planTitle
                    template[fullDayIndex].startingDate =
                        formatInstantToDate(
                            getMondayOrSameInstant(Clock.System.now()),
                            "dd-MM-yyyy HH:mm:ss"
                        )
                    fullDayIndex++
                }
            }
            viewModel.updateSelectedRoutineList(template)
        }
    }
}