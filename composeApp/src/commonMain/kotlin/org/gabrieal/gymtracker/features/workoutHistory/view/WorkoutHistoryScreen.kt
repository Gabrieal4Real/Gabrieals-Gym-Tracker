package org.gabrieal.gymtracker.features.workoutHistory.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.icon_reps
import gymtracker.composeapp.generated.resources.icon_sets
import gymtracker.composeapp.generated.resources.tier_0
import gymtracker.composeapp.generated.resources.tier_1
import gymtracker.composeapp.generated.resources.tier_2
import gymtracker.composeapp.generated.resources.tier_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.WorkoutHistory
import org.gabrieal.gymtracker.data.model.WorkoutProgress
import org.gabrieal.gymtracker.data.sqldelight.getAllWorkoutHistoryFromDB
import org.gabrieal.gymtracker.features.workoutHistory.viewmodel.WorkoutHistoryViewModel
import org.gabrieal.gymtracker.util.app.differenceTime
import org.gabrieal.gymtracker.util.app.planTitles
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.formatInstantToDate
import org.gabrieal.gymtracker.util.systemUtil.parseDateToInstant
import org.gabrieal.gymtracker.util.widgets.AnimatedDividerWithScale
import org.gabrieal.gymtracker.util.widgets.BiggerText
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.CustomHorizontalDivider
import org.gabrieal.gymtracker.util.widgets.CustomLineChart
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.TitleText
import org.gabrieal.gymtracker.util.widgets.popOutExtra
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime

object WorkoutHistoryScreen : Screen, KoinComponent {
    private val viewModel: WorkoutHistoryViewModel by inject()

    @Composable
    override fun Content() {
        val scope = rememberCoroutineScope()
        val uiState by viewModel.uiState.collectAsState()
        val expandedExercise = uiState.expandedExercise
        val groupedHistory = uiState.groupedAndSortedHistory
        val pagerState = rememberPagerState(pageCount = { planTitles.size })
        val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }
        val exercise = expandedExercise?.first
        val progress = expandedExercise?.second
        val index = expandedExercise?.third ?: 0

        LaunchedEffect(Unit) {
            viewModel.setWorkoutHistoryList(getAllWorkoutHistoryFromDB())
        }

        if (expandedExercise != null)
            Dialog(onDismissRequest = { viewModel.setExpandedExercise(null) }) {
                val weight = progress?.exerciseWeights?.getOrNull(index)
                val weightUnit = progress?.exerciseWeightUnit?.getOrNull(index)
                val reps = progress?.exerciseReps?.getOrNull(index)
                    ?.count { it.isNotBlank() && it.toInt() >= (exercise?.reps?.second ?: 0) } ?: 0
                val sets = progress?.exerciseSets?.getOrNull(index)?.count { it } ?: 0
                val setsText = "$sets out of ${exercise?.sets}"
                val repsText = "$reps out of ${exercise?.sets}"

                val listOfTriple = listOf(
                    Triple("Sets Completed", setsText, Res.drawable.icon_sets),
                    Triple("Max Reps Hit", repsText, Res.drawable.icon_reps)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 400.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.lighterBackground.copy(alpha = 0.7f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubtitleText(
                        exercise?.name.orEmpty().uppercase(),
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        TinyText(
                            exercise?.sets.toString() + " sets",
                            modifier = Modifier.padding(end = 12.dp, bottom = 4.dp)
                        )
                        BiggerText(
                            "${weight?.ifBlank { "0" }}${if (weightUnit == false) "lb" else "kg"}".uppercase(),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        TinyText(
                            exercise?.reps?.second.toString() + " reps",
                            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedDividerWithScale()
                    Spacer(modifier = Modifier.height(16.dp))

                    listOfTriple.forEach { triple ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(triple.third),
                                contentDescription = triple.third.toString(),
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(40.dp),
                                colorFilter = ColorFilter.tint(colors.textPrimary)
                            )

                            Spacer(modifier = Modifier.width(8.dp))
                            DashedDivider()
                            Spacer(modifier = Modifier.width(8.dp))

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center,
                            ) {
                                TinyText(triple.first, color = colors.textPrimary)
                                SubtitleText(triple.second, color = colors.textPrimary)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    getRankingForCompletion(sets, reps, exercise?.sets)
                }
            }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()
            VolumeGraph(groupedHistory, pagerState.currentPage)
            WorkoutTabs(planTitles, selectedTabIndex, pagerState, scope)
            CustomHorizontalDivider()
            WorkoutPages(groupedHistory, pagerState)
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
            divider = {}
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
        groupedHistory: Map<String, List<WorkoutHistory>>,
        pagerState: PagerState
    ) {
        Column(modifier = Modifier.fillMaxSize().background(colors.lighterBackground)) {
            HorizontalPager(state = pagerState) {
                val selectedWorkoutHistory = groupedHistory[planTitles[it]] ?: emptyList()
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    if (selectedWorkoutHistory.isEmpty()) {
                        SubtitleText("Your history looks empty".uppercase())
                        DescriptionItalicText("Why not start a new workout?")
                        return@HorizontalPager
                    }

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

                ExerciseGrid(workout.exercises.orEmpty(), workout.workoutProgress)

                Spacer(modifier = Modifier.height(8.dp))
                TinyItalicText("Completed in $elapsedTime")
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
                val weight = progress?.exerciseWeights?.getOrNull(index)
                val weightUnit = progress?.exerciseWeightUnit?.getOrNull(index)

                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.49f)
                        .defaultMinSize(minHeight = 110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            viewModel.setExpandedExercise(Triple(exercise, progress, exercises.indexOf(exercise)))
                        }
                        .background(colors.black.copy(alpha = 0.18f))
                        .padding(vertical = 8.dp, horizontal = 12.dp)
                ) {
                    TinyText(exercise.name.orEmpty(), modifier = Modifier.weight(1f))

                    val sets = progress?.exerciseSets?.getOrNull(index)?.count { it } ?: 0
                    val setsText = "$sets / ${exercise.sets} sets"

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 4.dp)
                            .height(18.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(colors.white.copy(alpha = 0.15f))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(sets / (exercise.sets?.toFloat() ?: 0f))
                                .fillMaxHeight()
                                .background(colors.checkMarkGreen)
                        )

                        TinyText(
                            setsText,
                            modifier = Modifier.align(Alignment.Center),
                            color = colors.textPrimary
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            modifier = Modifier.size(24.dp),
                            tint = colors.textPrimary.copy(alpha = 0.8f)
                        )

                        SubtitleText(
                            "${weight?.ifBlank { "0" }}${if (weightUnit == false) "lb" else "kg"}".uppercase(),
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun getRankingForCompletion(
        completedSets: Int,
        completedReps: Int,
        maxSets: Int?
    ) {
        val totalPoints = (completedSets + completedReps).toDouble()
        val maxPoints = ((maxSets ?: 0) + (maxSets ?: 0)).toDouble()

        val tier = when {
            totalPoints.div(maxPoints) >= 0.95 -> 0
            totalPoints.div(maxPoints) >= 0.8 -> 1
            totalPoints.div(maxPoints) >= 0.4 -> 2
            else -> 3
        }

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
            contentScale = ContentScale.FillHeight,
            modifier = Modifier.height(150.dp).scale(popOutExtra().value)
        )
    }

    @Composable
    fun VolumeGraph(groupedHistory: Map<String, List<WorkoutHistory>>, selectedTabIndex: Int) {
        val completedVolumes =
            if (groupedHistory.containsKey(planTitles[selectedTabIndex])) groupedHistory[planTitles[selectedTabIndex]]?.map { it.completedVolume }
                ?: emptyList() else emptyList()

        var isSampleData = false

        var pointsData = mutableListOf<Double>()
        pointsData.add(0.0)
        pointsData.addAll(completedVolumes.filterNotNull().asReversed())

        if (pointsData.isEmpty() || pointsData.size < 2) {
            isSampleData = true
            pointsData = MutableList(4) { (100..2000).random().toDouble() }
        }

        Box(modifier = Modifier.height(240.dp).fillMaxWidth().background(colors.lighterBackground).padding(vertical = 8.dp, horizontal = 12.dp)) {
            CustomLineChart(points = pointsData, modifier = Modifier.fillMaxSize().blur(if (isSampleData) 2.dp else 0.dp))

            if (isSampleData) {
                DescriptionText(
                    "Add more workouts to see volume graph",
                    modifier = Modifier.align(Alignment.Center).background(
                        shape = RoundedCornerShape(8.dp),
                        color = colors.lightMaroon.copy(alpha = 0.5f)
                    ).padding(8.dp),
                )
            }
        }
    }
}