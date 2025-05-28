package org.gabrieal.gymtracker.views.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material3.Icon
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
import org.gabrieal.gymtracker.util.appUtil.getCurrentPlan
import org.gabrieal.gymtracker.util.appUtil.longFormDays
import org.gabrieal.gymtracker.util.appUtil.planTitles
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.viewmodel.makeAPlan.MakeAPlanViewModel
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.templates
import org.gabrieal.gymtracker.views.widgets.AnimatedImage
import org.gabrieal.gymtracker.views.widgets.BackButtonRow
import org.gabrieal.gymtracker.views.widgets.ButtonType
import org.gabrieal.gymtracker.views.widgets.ConfirmButton
import org.gabrieal.gymtracker.views.widgets.CustomCard
import org.gabrieal.gymtracker.views.widgets.DescriptionText
import org.gabrieal.gymtracker.views.widgets.SubtitleText
import org.gabrieal.gymtracker.views.widgets.TinyItalicText
import org.gabrieal.gymtracker.views.widgets.TinyText

object MakeAPlanScreen : Screen {
    private val viewModel = MakeAPlanViewModel()

    fun setSelectedDay(selectedDays: List<Boolean>) {
        viewModel.setSelectedDays(selectedDays)
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
        val saveRoutineList = uiState.saveRoutineList

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
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
                                                contentDescription = null,
                                                tint = colors.textPrimary
                                            )
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
                                viewModel.setSaveRoutineList(true)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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

                // Process save routine list
                if (saveRoutineList) {
                    viewModel.saveRoutineList()
                }
            }
        }
    }

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
                    fullDayIndex++
                }
            }
            viewModel.updateSelectedRoutineList(template)
        }
    }
}