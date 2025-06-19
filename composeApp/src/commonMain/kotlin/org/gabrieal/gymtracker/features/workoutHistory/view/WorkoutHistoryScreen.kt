package org.gabrieal.gymtracker.features.workoutHistory.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowColumn
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.icon_reps
import gymtracker.composeapp.generated.resources.icon_sets
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import network.chaintech.chartsLib.ui.linechart.model.IntersectionPoint
import network.chaintech.cmpcharts.axis.AxisProperties
import network.chaintech.cmpcharts.common.extensions.formatToSinglePrecision
import network.chaintech.cmpcharts.common.model.Point
import network.chaintech.cmpcharts.common.ui.GridLinesUtil
import network.chaintech.cmpcharts.common.ui.SelectionHighlightPoint
import network.chaintech.cmpcharts.common.ui.SelectionHighlightPopUp
import network.chaintech.cmpcharts.common.ui.ShadowUnderLine
import network.chaintech.cmpcharts.ui.linechart.LineChart
import network.chaintech.cmpcharts.ui.linechart.model.Line
import network.chaintech.cmpcharts.ui.linechart.model.LineChartProperties
import network.chaintech.cmpcharts.ui.linechart.model.LinePlotData
import network.chaintech.cmpcharts.ui.linechart.model.LineStyle
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.WorkoutHistory
import org.gabrieal.gymtracker.data.model.WorkoutProgress
import org.gabrieal.gymtracker.data.sqldelight.getAllWorkoutHistoryFromDB
import org.gabrieal.gymtracker.features.workoutHistory.viewmodel.WorkoutHistoryViewModel
import org.gabrieal.gymtracker.util.app.RegularText
import org.gabrieal.gymtracker.util.app.differenceTime
import org.gabrieal.gymtracker.util.app.planTitles
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.formatInstantToDate
import org.gabrieal.gymtracker.util.systemUtil.parseDateToInstant
import org.gabrieal.gymtracker.util.widgets.AnimatedDividerWithScale
import org.gabrieal.gymtracker.util.widgets.BigText
import org.gabrieal.gymtracker.util.widgets.BiggerText
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.CustomHorizontalDivider
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.TitleText
import org.gabrieal.gymtracker.util.widgets.popOut
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
        val groupedHistory = uiState.groupedAndSortedHistory
        val pagerState = rememberPagerState(pageCount = { planTitles.size })
        val selectedTabIndex by remember { derivedStateOf { pagerState.currentPage } }

        LaunchedEffect(Unit) {
            viewModel.setWorkoutHistoryList(getAllWorkoutHistoryFromDB())
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Header()
            VolumeGraph(groupedHistory, pagerState.currentPage)
            WorkoutTabs(planTitles, selectedTabIndex, pagerState, scope)
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
            divider = {
                CustomHorizontalDivider()
            }
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

                ExerciseGrid(
                    exercises = workout.exercises.orEmpty(),
                    progress = workout.workoutProgress
                )

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
        var expandedExercise by remember { mutableStateOf<SelectedExercise?>(null) }

        FlowColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            maxItemsInEachColumn = 2,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            exercises.forEachIndexed { index, exercise ->
                val weight = progress?.exerciseWeights?.getOrNull(index)

                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.49f)
                        .defaultMinSize(minHeight = 110.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable {
                            expandedExercise = exercise
                        }
                        .background(colors.black.copy(alpha = 0.18f))
                        .padding(vertical = 8.dp, horizontal = 12.dp)

                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        DescriptionText(exercise.name.orEmpty())
                    }

                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = "Search",
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .size(24.dp),
                        tint = colors.textPrimary.copy(alpha = 0.8f)
                    )

                    SubtitleText(
                        "${weight?.ifBlank { "0" }}kg".uppercase(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomEnd),
                        textAlign = TextAlign.End
                    )
                }
            }
        }

        expandedExercise?.let { exercise ->
            Dialog(onDismissRequest = { expandedExercise = null }) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 400.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(colors.lighterBackground.copy(alpha = 0.8f))
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubtitleText(
                        exercise.name.orEmpty().uppercase(),
                        color = colors.textPrimary,
                        textAlign = TextAlign.Center
                    )

                    val index = exercises.indexOf(exercise)
                    val weight = progress?.exerciseWeights?.getOrNull(index)

                    BiggerText(
                        "${weight?.ifBlank { "0" }}kg",
                        modifier = Modifier.padding(top = 8.dp).scale(popOut().value)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    AnimatedDividerWithScale()
                    Spacer(modifier = Modifier.height(16.dp))

                    val sets = Triple(
                        "Sets Completed",
                        "${progress?.exerciseSets?.getOrNull(index)?.count { it } ?: 0} out of ${exercise.sets}",
                        Res.drawable.icon_sets
                    )
                    val reps = Triple(
                        "Max Reps Hit",
                        "${progress?.exerciseReps?.getOrNull(index)?.count { it.isNotBlank() && it.toInt() >= (exercise.reps?.second ?: 0) } ?: 0} out of ${exercise.sets}",
                        Res.drawable.icon_reps
                    )

                    val listOfTriple = listOf(sets, reps)

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
                }
            }
        }
    }

    @Composable
    fun VolumeGraph(groupedHistory: Map<String, List<WorkoutHistory>>, selectedTabIndex: Int) {
        val completedVolumes =
            if (groupedHistory.containsKey(planTitles[selectedTabIndex])) groupedHistory[planTitles[selectedTabIndex]]?.map { it.completedVolume }
                ?: emptyList() else emptyList()

        var isSampleData = false
        var pointsData = completedVolumes.filterNotNull().asReversed().mapIndexed { index, volume ->
            Point(
                x = index.toFloat() + 1,
                y = volume.toFloat()
            )
        }

        if (pointsData.isEmpty() || pointsData.size < 8) {
            isSampleData = true
            pointsData = List(8) { index ->
                Point(
                    x = index.toFloat() + 1,
                    y = (100..2000).random().toFloat()
                )
            }
        }

        val textMeasurer = rememberTextMeasurer()

        val steps = 4
        val xAxisProperties = AxisProperties(
            font = RegularText(),
            shouldExtendLineToEnd = true,
            stepSize = 48.dp,
            initialDrawPadding = 8.dp,
            labelFontSize = 12.sp,
            labelPadding = 4.dp,
            labelColor = colors.white,
            lineColor = colors.white,
            stepCount = pointsData.size - 1,
            labelFormatter = { i -> pointsData[i].x.toInt().toString() },
        )

        val yAxisProperties = AxisProperties(
            font = RegularText(),
            initialDrawPadding = 20.dp,
            labelFontSize = 12.sp,
            labelPadding = 4.dp,
            stepCount = steps,
            labelColor = colors.white,
            lineColor = colors.white,
            labelFormatter = { i ->
                val yMin = pointsData.minOf { it.y }
                val yMax = pointsData.maxOf { it.y }
                val yScale = (yMax - yMin) / steps
                ((i * yScale) + yMin).formatToSinglePrecision()
            }
        )

        val lineChartProperties = LineChartProperties(
            paddingTop = 16.dp,
            bottomPadding = 12.dp,
            backgroundColor = colors.lighterBackground,
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = pointsData,
                        LineStyle(color = colors.slightlyDarkerLinkBlue),
                        IntersectionPoint(color = colors.slightlyDarkerLinkBlue),
                        SelectionHighlightPoint(color = colors.lightMaroon),
                        ShadowUnderLine(),
                        SelectionHighlightPopUp(
                            textMeasurer = textMeasurer,
                            backgroundColor = colors.maroon,
                            labelColor = colors.white,
                            labelTypeface = FontWeight.Bold
                        )
                    )
                )
            ),
            xAxisProperties = xAxisProperties,
            yAxisProperties = yAxisProperties,
            gridLines = GridLinesUtil(color = colors.placeholderColor)
        )

        Box(modifier = Modifier.height(220.dp).fillMaxWidth()) {
            LineChart(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colors.lighterBackground).blur(if (isSampleData) 2.dp else 0.dp),
                lineChartProperties = lineChartProperties
            )

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