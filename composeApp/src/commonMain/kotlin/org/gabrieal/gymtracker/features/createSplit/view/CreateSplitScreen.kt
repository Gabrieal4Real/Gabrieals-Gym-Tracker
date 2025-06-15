package org.gabrieal.gymtracker.features.createSplit.view

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.RadioButton
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
import gymtracker.composeapp.generated.resources.new_to_workout
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.features.createSplit.viewmodel.CreateSplitViewModel
import org.gabrieal.gymtracker.util.app.plans
import org.gabrieal.gymtracker.util.app.shortFormDays
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.widgets.AnimatedImage
import org.gabrieal.gymtracker.util.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.widgets.BigText
import org.gabrieal.gymtracker.util.widgets.ConfirmButton
import org.gabrieal.gymtracker.util.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TitleRow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CreateSplitScreen : Screen, KoinComponent {
    private val viewModel: CreateSplitViewModel by inject()

    fun setRoutines(routines: List<SelectedExerciseList>) = viewModel.isEditMode(routines)

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val selectedDays = uiState.selectedDays
        var showImage = uiState.showImage
        val isEditMode = uiState.isEditMode


        if (!isEditMode)
            BackHandler(enabled = true) {}

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (isEditMode) BackButtonRow("Edit Split") else TitleRow(Resources.strings.createSplit)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.lighterBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.92f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                ) {
                    SubtitleText(Resources.strings.howManyDays)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Day Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        shortFormDays.forEachIndexed { index, day ->
                            val isSelected = selectedDays[index]
                            Card(
                                shape = RoundedCornerShape(4.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) colors.white else colors.maroon
                                ),
                                elevation = CardDefaults.elevatedCardElevation(
                                    defaultElevation = if (isSelected) 8.dp else 4.dp
                                ),
                                modifier = Modifier
                                    .size(36.dp)
                                    .clickable {
                                        viewModel.updateSelectedDay(index, !isSelected)
                                    }
                            ) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    BigText(
                                        day,
                                        color = if (isSelected) colors.black else colors.white
                                    )
                                }
                            }
                        }
                    }

                    // Rest Day Indicator
                    DescriptionText(
                        Resources.strings.redsIndicateRest,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // Recommended Split
                    SubtitleText(Resources.strings.recommendedSplit)
                    Spacer(modifier = Modifier.height(8.dp))
                    TinyItalicText(
                        Resources.strings.xDayWorkoutxDayRest(
                            selectedDays.count { it },
                            selectedDays.count { !it }),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    var position = 0
                    selectedDays.forEachIndexed { index, isSelected ->
                        if (isSelected) {
                            DescriptionText("Day ${index + 1}: ${plans[selectedDays.count { it } - 1][position]}")
                            position += 1
                        } else {
                            DescriptionItalicText(
                                "Day ${index + 1}: Rest",
                                color = colors.lightMaroon
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Confirm Button
                ConfirmButton(
                    Resources.strings.letsPlanIt,
                    onClick = { viewModel.navigateToMakeAPlan() },
                    enabled = viewModel.isAnyDaySelected(),
                    modifier = Modifier.fillMaxWidth().align(Alignment.BottomEnd).padding(16.dp)
                )

                // Animated Image
                if (!isEditMode) {
                    showImage = AnimatedImage(showImage, Res.drawable.new_to_workout, false)
                    viewModel.setShowImage(showImage)
                }
            }
        }
    }
}
