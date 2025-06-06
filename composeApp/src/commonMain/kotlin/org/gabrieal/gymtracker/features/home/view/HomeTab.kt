package org.gabrieal.gymtracker.features.home.view

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.util.lerp
import cafe.adriel.voyager.core.annotation.InternalVoyagerApi
import cafe.adriel.voyager.navigator.internal.BackHandler
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.habit_1
import gymtracker.composeapp.generated.resources.habit_2
import gymtracker.composeapp.generated.resources.habit_3
import gymtracker.composeapp.generated.resources.habit_4
import gymtracker.composeapp.generated.resources.habit_5
import gymtracker.composeapp.generated.resources.habit_6
import gymtracker.composeapp.generated.resources.habit_7
import gymtracker.composeapp.generated.resources.habit_8
import gymtracker.composeapp.generated.resources.icon_protein
import gymtracker.composeapp.generated.resources.workout_1
import gymtracker.composeapp.generated.resources.workout_2
import gymtracker.composeapp.generated.resources.workout_3
import gymtracker.composeapp.generated.resources.workout_4
import gymtracker.composeapp.generated.resources.workout_5
import gymtracker.composeapp.generated.resources.workout_7
import gymtracker.composeapp.generated.resources.workout_8
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.features.home.viewmodel.HomeViewModel
import org.gabrieal.gymtracker.util.app.longFormDays
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.util.systemUtil.getTodayDayName
import org.gabrieal.gymtracker.util.widgets.BiggerText
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.DescriptionItalicText
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.LinkText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyItalicText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.gabrieal.gymtracker.util.widgets.TitleRow
import org.jetbrains.compose.resources.painterResource

object HomeTab : Tab {
    private val viewModel = HomeViewModel()

    private val randomSelectedWorkoutImage = listOf(
        Res.drawable.workout_1,
        Res.drawable.workout_2,
        Res.drawable.workout_3,
        Res.drawable.workout_4,
        Res.drawable.workout_5,
        Res.drawable.workout_7,
        Res.drawable.workout_8
    ).random()

    private val randomSelectedHabitImage = listOf(
        Res.drawable.habit_1,
        Res.drawable.habit_2,
        Res.drawable.habit_3,
        Res.drawable.habit_4,
        Res.drawable.habit_5,
        Res.drawable.habit_6,
        Res.drawable.habit_7,
        Res.drawable.habit_8
    ).random()

    private val restDayMessage = listOf(
        "Time to recharge!\nRest up and treat yourself today.\n🔋🍰😴",
        "Kick back, relax, and let your body recover.\n🧘‍♂️💤🛀",
        "Netflix, snacks, and gains incoming.\n📺🍿🔥",
        "Even superheroes take a break—enjoy your rest day!\n🦸‍♂️🛌✨",
        "Take a deep breath, prioritise recovery and self-care.\n🌿🫁🕯️",
        "Your body grows when you rest.\nEmbrace the pause and feel good today.\n🌙💪🧠"
    ).random()

    @OptIn(InternalVoyagerApi::class, ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val selectedRoutineList = uiState.selectedRoutineList

        val todayRoutine =
            selectedRoutineList.find { it.day.equals(getTodayDayName(), ignoreCase = false) }
        val followingDay =
            longFormDays[(longFormDays.indexOf(getTodayDayName()) + 1) % longFormDays.size]
        val followingDayRoutine =
            selectedRoutineList.find { it.day.equals(followingDay, ignoreCase = false) }

        val initialAspectRatio = 0.95f
        val maxCollapsedAspectRatio = 4.5f

        val scrollState = rememberLazyListState()
        val headerHeightPx = with(LocalDensity.current) { 80.dp.toPx() }
        val totalScroll =
            scrollState.firstVisibleItemIndex * headerHeightPx + scrollState.firstVisibleItemScrollOffset
        val collapseFraction = (totalScroll / headerHeightPx).coerceIn(0f, 1f)
        val currentAspectRatio = lerp(initialAspectRatio, maxCollapsedAspectRatio, collapseFraction)
        val currentSpacerHeight = lerp(0.dp, 80.dp, collapseFraction)
        val currentBackgroundOpacity = lerp(0f, 1f, collapseFraction)
        val animateCurrentAspectRatio by animateFloatAsState(targetValue = currentAspectRatio)
        val animateCurrentBackgroundOpacity by animateFloatAsState(targetValue = currentBackgroundOpacity)

        val context = getCurrentContext()

        BackHandler(enabled = true) {}

        LaunchedEffect(context) {
            viewModel.updateContext(context)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleRow(Resources.strings.appName)
            Box(modifier = Modifier.fillMaxSize().background(colors.lighterBackground)) {
                if (selectedRoutineList.isEmpty()) {
                    return@Box
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    LazyColumn(state = scrollState, modifier = Modifier.fillMaxWidth()) {
                        stickyHeader {
                            WorkoutHeader(
                                animateCurrentAspectRatio,
                                animateCurrentBackgroundOpacity,
                                todayRoutine
                            )
                        }
                        item {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Spacer(modifier = Modifier.height(currentSpacerHeight))
                                // Progress Summary
                                ProgressSummary(selectedRoutineList)
                                Spacer(modifier = Modifier.height(16.dp))

                                // Reminder Image
                                ReminderImage(
                                    modifier = Modifier.fillMaxWidth()
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(16.dp))

                                // Next Workout Preview
                                NextWorkoutPreview(followingDayRoutine)
                                Spacer(modifier = Modifier.height(16.dp))

                                // Habit Image
                                Image(
                                    painter = painterResource(randomSelectedHabitImage),
                                    contentDescription = "Habit",
                                    contentScale = ContentScale.FillHeight,
                                    modifier = Modifier.height(220.dp)
                                        .align(Alignment.CenterHorizontally)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                // Last Workout Highlight
                                LastWorkoutHighlight(selectedRoutineList)
                            }
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun LastWorkoutHighlight(selectedRoutineList: List<SelectedExerciseList>) {
        CustomCard(
            enabled = true,
            content = {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubtitleText("PR of the week".uppercase())
                    //check if any routine of the week is completed
                    val completedRoutine = selectedRoutineList.find { it.isCompleted }
                    if (completedRoutine != null) {
                        BiggerText(completedRoutine.routineName ?: "Rest")
                        Spacer(modifier = Modifier.height(2.dp))
                        TinyItalicText(completedRoutine.exercises?.joinToString(", ") { it.name.orEmpty() }
                            ?: "", textAlign = TextAlign.Center)
                        return@Column
                    }

                    TinyItalicText(
                        "You have not completed any workouts this week",
                        textAlign = TextAlign.Center
                    )
                }
            },
        )
    }

    @Composable
    fun NextWorkoutPreview(followingDayRoutine: SelectedExerciseList?) {
        CustomCard(
            enabled = followingDayRoutine != null,
            content = {
                Column(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubtitleText("What's for tomorrow".uppercase())
                    BiggerText(followingDayRoutine?.routineName ?: "Rest")
                    Spacer(modifier = Modifier.height(2.dp))
                    if (followingDayRoutine != null) {
                        TinyItalicText(followingDayRoutine.exercises?.joinToString(", ") { it.name.orEmpty() }
                            ?: "", textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(8.dp))
                        LinkText("Click for more details")
                        return@Column
                    }

                    TinyItalicText(restDayMessage, textAlign = TextAlign.Center)
                }
            },
            onClick = {
                followingDayRoutine?.let {
                    viewModel.navigateToStartWorkout(it) {
                        viewModel.updateSelectedRoutine(it)
                    }
                }
            }
        )
    }

    @Composable
    fun ReminderImage(modifier: Modifier) {
        Row(
            modifier = modifier.padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(Res.drawable.icon_protein),
                contentDescription = "Protein",
                contentScale = ContentScale.Fit,
                modifier = Modifier.height(120.dp),
                colorFilter = ColorFilter.tint(colors.textPrimary)
            )
            DescriptionText(
                "Don’t forget to take your protein and stay hydrated—your body will thank you!",
                modifier = Modifier.padding(end = 16.dp)
            )
        }
    }

    @Composable
    fun ProgressSummary(selectedRoutineList: List<SelectedExerciseList>) {
        CustomCard(
            enabled = true,
            content = {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    SubtitleText("This Week In Recap".uppercase())
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        selectedRoutineList.forEachIndexed { index, it ->
                            RoutineStatusColumn(it)

                            if (index != selectedRoutineList.lastIndex) {
                                Spacer(modifier = Modifier.width(8.dp))
                                DashedDivider()
                                Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    TinyItalicText("Workouts: ${selectedRoutineList.count { it.isCompleted }} / ${selectedRoutineList.size} completed")
                }
            },
        )
    }

    @Composable
    fun RoutineStatusColumn(routine: SelectedExerciseList) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
            viewModel.navigateToStartWorkout(routine) {
                viewModel.updateSelectedRoutine(it)
            }
        }) {
            Spacer(modifier = Modifier.height(2.dp))
            DescriptionText(if (routine.isCompleted) "✅" else "⬜")
            Spacer(modifier = Modifier.height(2.dp))
            TinyText(routine.routineName ?: "")
        }
    }

    @Composable
    fun WorkoutHeader(
        animateCurrentAspectRatio: Float,
        animateCurrentBackgroundOpacity: Float,
        selectedRoutine: SelectedExerciseList?
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(animateCurrentAspectRatio)
                .clickable {
                    selectedRoutine?.let {
                        viewModel.navigateToStartWorkout(it) {
                            viewModel.updateSelectedRoutine(it)
                        }
                    }
                }
        ) {
            Image(
                painter = painterResource(randomSelectedWorkoutImage),
                contentDescription = "Workout image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize()
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                colors.black.copy(alpha = 1f)
                            )
                        )
                    )
            )

            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(
                                Color.Transparent,
                                colors.black.copy(alpha = animateCurrentBackgroundOpacity)
                            )
                        )
                    ),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    DescriptionItalicText("Today's Workout")
                    BiggerText(selectedRoutine?.routineName ?: "Rest Day")
                }

                BiggerText(">")
            }
        }
    }

    override val options: TabOptions
        @Composable get() = TabOptions(
            index = 1u,
            title = "Home",
            icon = rememberVectorPainter(Icons.Rounded.Home),
        )
}
