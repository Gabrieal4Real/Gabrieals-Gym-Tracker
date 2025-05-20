package org.gabrieal.gymtracker.views.screens

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
import org.gabrieal.gymtracker.util.appUtil.Workout
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.days
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.viewmodel.createSplit.CreateSplitViewModel
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.widgets.AnimatedImage
import org.gabrieal.gymtracker.views.widgets.BackButtonRow
import org.gabrieal.gymtracker.views.widgets.BigText
import org.gabrieal.gymtracker.views.widgets.ConfirmButton
import org.gabrieal.gymtracker.views.widgets.DescriptionText
import org.gabrieal.gymtracker.views.widgets.SubtitleText
import org.gabrieal.gymtracker.views.widgets.TitleRow

object CreateSplitScreen : Screen {
    private val viewModel = CreateSplitViewModel()
    
    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val selectedDays = uiState.selectedDays
        var showImage = uiState.showImage

        BackHandler(enabled = true) {

        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleRow(Resources.strings.createSplit)

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.lighterBackground)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.9f)
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
                        days.forEachIndexed { index, day ->
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
                                    BigText(day, color = if (isSelected) colors.black else colors.white)
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
                    Workout.WorkoutSplit(selectedDays)
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
                showImage = AnimatedImage(showImage, Res.drawable.new_to_workout, false)
                viewModel.setShowImage(showImage)
            }
        }
    }
}
