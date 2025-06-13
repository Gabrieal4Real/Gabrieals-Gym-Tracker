package org.gabrieal.gymtracker.features.viewAllWorkouts.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.tier_0
import gymtracker.composeapp.generated.resources.tier_1
import gymtracker.composeapp.generated.resources.tier_2
import gymtracker.composeapp.generated.resources.tier_3
import gymtracker.composeapp.generated.resources.youtube
import org.gabrieal.gymtracker.allExistingExerciseList
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.features.viewAllWorkouts.viewmodel.ViewAllWorkoutViewModel
import org.gabrieal.gymtracker.util.systemUtil.OpenURL
import org.gabrieal.gymtracker.util.systemUtil.ShowAlertDialog
import org.gabrieal.gymtracker.util.widgets.BackButtonRow
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.CustomTextField
import org.gabrieal.gymtracker.util.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.DropDownFilter
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TitleRow
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object ViewAllWorkoutTabScreen : Tab, Screen, KoinComponent {
    private val viewModel: ViewAllWorkoutViewModel by inject()

    fun setCallback(onMessageSent: (String) -> Unit) {
        viewModel.setCallback(onMessageSent)
    }

    @OptIn(InternalVoyagerApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        LaunchedEffect(allExistingExerciseList) {
            viewModel.updateFilteredWorkouts()
        }

        val searchFilter = uiState.searchFilter
        val selectedFilters = uiState.selectedFilters
        val selectedWorkout = uiState.selectedWorkout
        val showConfirmAddToRoutineDialog = uiState.showConfirmAddToRoutineDialog
        val filteredWorkouts = uiState.filteredWorkouts
        val callback = uiState.callback

        val allMuscleGroups = viewModel.getAllMuscleGroups()

        uiState.youtubeUrlToOpen?.let { url ->
            OpenURL(url)
            viewModel.onUrlOpened()
        }

        if (callback == null) {
            BackHandler(enabled = false) {}
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (callback != null) BackButtonRow("All Workouts")
            else TitleRow("All Workouts")

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.lighterBackground)
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp)
            ) {
                DropDownFilter(
                    filterOptions = allMuscleGroups,
                    selectedFilters = selectedFilters,
                    onFilterSelected = { filter ->
                        viewModel.toggleFilter(filter)
                    },
                    modifier = Modifier.align(Alignment.TopEnd)
                )

                Column {
                    // Section title and filter instructions
                    SubtitleText("All Workouts")
                    Spacer(modifier = Modifier.height(8.dp))
                    DescriptionItalicText("Select muscle groups to filter by")
                    Spacer(modifier = Modifier.height(8.dp))

                    // Search field for filtering by workout name
                    CustomTextField(
                        value = searchFilter,
                        onValueChange = { viewModel.setSearchFilter(it) },
                        placeholderText = "Search exercises..."
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    // List of filtered workouts
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredWorkouts.size) { idx ->
                            val workout = filteredWorkouts[idx]
                            CustomCard(
                                enabled = true,
                                onClick = {
                                    viewModel.setSelectedWorkout(workout.name)
                                },
                                content = {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(
                                            modifier = Modifier
                                                .weight(1f)
                                                .padding(16.dp)
                                        ) {
                                            // Workout name and muscle group tags
                                            DescriptionText(workout.name)
                                            TinyItalicText(
                                                workout.targetMuscle,
                                                color = colors.textSecondary
                                            )
                                            // YouTube icon to search for workout demo
                                            Image(
                                                painter = painterResource(Res.drawable.youtube),
                                                contentDescription = "Youtube",
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .padding(top = 8.dp)
                                                    .clickable {
                                                        viewModel.openYoutubeSearch(workout.name)
                                                    }
                                            )
                                        }
                                        // Workout tier badge
                                        TierImage(workout.tier)
                                    }
                                })
                        }
                    }
                }

                // Confirmation dialog for adding workout to routine
                if (showConfirmAddToRoutineDialog && callback != null) {
                    ShowAlertDialog(
                        titleMessage = Pair(
                            "Add Workout?",
                            "Do you want to add $selectedWorkout to your routine?"
                        ),
                        positiveButton = Pair("Proceed") {
                            viewModel.confirmWorkoutSelection()
                        },
                        negativeButton = Pair("Cancel") {
                            viewModel.dismissConfirmDialog()
                        }
                    )
                }
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
            contentDescription = "Tier $tier",
            modifier = Modifier.size(100.dp).padding(end = 8.dp)
        )
    }

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 0u,
            title = "Workouts",
            icon = rememberVectorPainter(Icons.Rounded.Search),
        )
}