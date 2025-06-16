package org.gabrieal.gymtracker.features.workoutHistory.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.WorkoutHistory
import org.gabrieal.gymtracker.data.model.WorkoutProgress
import org.gabrieal.gymtracker.data.sqldelight.getAllWorkoutHistoryFromDB
import org.gabrieal.gymtracker.features.workoutHistory.viewmodel.WorkoutHistoryViewModel
import org.gabrieal.gymtracker.util.app.differenceTime
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.formatInstantToDate
import org.gabrieal.gymtracker.util.systemUtil.parseDateToInstant
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.CustomHorizontalDivider
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.TitleText
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

object WorkoutHistoryScreen : Screen, KoinComponent {
    private val viewModel: WorkoutHistoryViewModel by inject()

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val uiState by viewModel.uiState.collectAsState()
        val groupedHistory = uiState.groupedAndSortedHistory
        val pagerState = rememberPagerState(pageCount = { groupedHistory.keys.size })
        val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

        LaunchedEffect(Unit) {
            viewModel.setWorkoutHistoryList(getAllWorkoutHistoryFromDB())
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()
            WorkoutTabs(groupedHistory.keys.toList(), selectedTabIndex, pagerState, scope)
            WorkoutPages(groupedHistory.values.toList(), pagerState)
        }
    }

    @Composable
    private fun Header() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { AppNavigator.navigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = "Back",
                    tint = colors.textPrimary
                )
            }
            TitleText("Workout History")
        }
    }

    @Composable
    private fun WorkoutTabs(
        tabTitles: List<String>,
        selectedTabIndex: Int,
        pagerState: PagerState,
        scope: CoroutineScope
    ) {
        ScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 16.dp,
            containerColor = colors.background,
            contentColor = Color.Transparent,
            indicator = { tabPositions ->
                if (selectedTabIndex < tabPositions.size) {
                    TabRowDefaults.PrimaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = colors.borderStroke
                    )
                }
            },
            divider = { CustomHorizontalDivider() }
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { TinyText(title) }
                )
            }
        }
    }

    @Composable
    private fun WorkoutPages(
        historyGroups: List<List<WorkoutHistory>>,
        pagerState: PagerState
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.lighterBackground)
        ) {
            HorizontalPager(state = pagerState) { page ->
                val selectedWorkoutHistory = historyGroups[page]

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedWorkoutHistory.size) { index ->
                        WorkoutCard(selectedWorkoutHistory[index])
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    private fun WorkoutCard(workout: WorkoutHistory) {
        CustomCard(enabled = true) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val startedOn = workout.startedOn?.let {
                    parseDateToInstant(it, "dd-MM-yyyy HH:mm:ss")
                }
                val finishedOn = parseDateToInstant(workout.finishedDate, "dd-MM-yyyy HH:mm:ss")
                val elapsedTime = differenceTime(startedOn, finishedOn)
                val formattedDate = startedOn?.let {
                    formatInstantToDate(it, "dd MMMM yyyy hh:mm a")
                } ?: ""

                TinyText("Started on")
                SubtitleText(formattedDate.uppercase())

                ExerciseGrid(
                    exercises = workout.exercises.orEmpty(),
                    progress = workout.workoutProgress
                )

                Spacer(modifier = Modifier.height(8.dp))
                TinyText("Completed in $elapsedTime")
            }
        }
    }

    @Composable
    private fun ExerciseGrid(
        exercises: List<SelectedExercise>,
        progress: WorkoutProgress?
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            maxItemsInEachRow = 2,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            exercises.forEachIndexed { index, exercise ->
                val sets = progress?.exerciseSets?.getOrNull(index)
                val reps = progress?.exerciseReps?.getOrNull(index)
                val weight = progress?.exerciseWeights?.getOrNull(index)

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.49f)
                        .height(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(colors.lighterBackground.copy(alpha = 0.5f))
                        .padding(8.dp)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        TinyText(exercise.name.orEmpty())

                        Row {
                            sets?.forEach { isComplete ->
                                TinyText(
                                    if (isComplete) "✅" else "❌",
                                    modifier = Modifier.padding(end = 4.dp)
                                )
                            }
                        }

                        Row {
                            reps?.forEach { rep ->
                                TinyText(
                                    rep.ifBlank { "-" },
                                    modifier = Modifier.padding(end = 10.dp)
                                )
                            }
                        }
                    }

                    SubtitleText(
                        (weight?.ifBlank { "0" } + "kg").uppercase(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd),
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}