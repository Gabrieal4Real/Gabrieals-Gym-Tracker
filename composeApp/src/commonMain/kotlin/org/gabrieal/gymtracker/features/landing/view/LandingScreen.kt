package org.gabrieal.gymtracker.features.landing.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.PauseCircleFilled
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PlayCircleFilled
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material.icons.rounded.Start
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import coil3.compose.AsyncImage
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.workout_1
import gymtracker.composeapp.generated.resources.workout_3
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.currentlyActiveRoutine
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.SpotifyPlayback
import org.gabrieal.gymtracker.data.model.SpotifyPlayerState
import org.gabrieal.gymtracker.data.sqldelight.getCurrentlyActiveRoutineFromDB
import org.gabrieal.gymtracker.data.sqldelight.getSelectedRoutineListFromDB
import org.gabrieal.gymtracker.data.sqldelight.setSelectedRoutineListToDB
import org.gabrieal.gymtracker.features.home.view.HomeTab
import org.gabrieal.gymtracker.features.landing.viewmodel.LandingViewModel
import org.gabrieal.gymtracker.features.profile.view.ProfileTab
import org.gabrieal.gymtracker.features.viewAllWorkouts.view.ViewAllWorkoutTabScreen
import org.gabrieal.gymtracker.util.app.ElapsedTime
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.ShowToast
import org.gabrieal.gymtracker.util.systemUtil.openNowPlaying
import org.gabrieal.gymtracker.util.widgets.DotsIndicator
import org.gabrieal.gymtracker.util.widgets.MarqueeSubtitleText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.jetbrains.compose.resources.painterResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object LandingScreen : Screen, KoinComponent {
    private val viewModel: LandingViewModel by inject()

    @OptIn(ExperimentalMaterialApi::class, ExperimentalTime::class)
    @Composable
    override fun Content() {
        val lifecycleOwner = LocalLifecycleOwner.current

        val uiState by viewModel.uiState.collectAsState()
        val landingCurrentlyActiveRoutine = uiState.currentlyActiveRoutine
        val resetCompletedList = uiState.resetCompletedList
        val currentPlayback = uiState.spotifyPlayback

        LaunchedEffect(uiState.currentlyActiveRoutine) {
            currentlyActiveRoutine = getCurrentlyActiveRoutineFromDB()
            viewModel.setCurrentlyActiveRoutine(currentlyActiveRoutine?.first)
            viewModel.resetCompletedList()
        }

        LaunchedEffect(lifecycleOwner) {
            lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.needRefreshCurrentPlayback()
            }
        }

        BottomSheetNavigator(
            sheetBackgroundColor = Color.Transparent,
            sheetContentColor = Color.Transparent,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) { bottomSheetNavigator ->
            LaunchedEffect(bottomSheetNavigator) {
                AppNavigator.setBottomSheetNavigator(bottomSheetNavigator)
            }
            TabNavigator(HomeTab) { tabNavigator ->
                Scaffold(
                    bottomBar = {
                        Column {
                            NavigationBar(
                                containerColor = colors.background,
                            ) {
                                TabNavigationItem(ViewAllWorkoutTabScreen)
                                TabNavigationItem(HomeTab)
                                TabNavigationItem(ProfileTab)
                            }
                        }
                    }
                ) {
                    Box(modifier = Modifier.padding(it).background(colors.background)) {
                        Crossfade(targetState = tabNavigator.current) { tab ->
                            tab.Content()
                        }

                        ActiveWorkoutAndPlaybackPager(
                            landingCurrentlyActiveRoutine,
                            currentlyActiveRoutine?.second,
                            currentPlayback,
                            Modifier.align(Alignment.BottomCenter)
                        )
                    }
                }
            }
        }

        if (resetCompletedList) {
            ShowToast("It's a brand new week! Your completed workouts have been reset.")
            viewModel.setCompletedRoutineList(false)
        }
    }


    @OptIn(ExperimentalTime::class)
    @Composable
    fun ActiveWorkoutAndPlaybackPager(
        landingCurrentlyActiveRoutine: SelectedExerciseList?,
        elapsedTime: Instant?,
        currentPlayback: SpotifyPlayback?,
        modifier: Modifier = Modifier
    ) {
        val pages = mutableListOf<@Composable () -> Unit>()

        val pageModifier =
            Modifier.padding(horizontal = 6.dp).clip(RoundedCornerShape(6.dp)).height(60.dp)

        if (landingCurrentlyActiveRoutine != null) {
            pages.add({
                CurrentlyActiveWorkout(
                    landingCurrentlyActiveRoutine,
                    elapsedTime,
                    pageModifier
                )
            })
        }

        if (currentPlayback != null) {
            pages.add({ CurrentlyPlaying(currentPlayback, pageModifier) })
        }

        if (pages.isNotEmpty()) {
            val pagerState = rememberPagerState { pages.size }

            Box(modifier = modifier.padding(vertical = 6.dp)) {
                HorizontalPager(state = pagerState) { page ->
                    pages[page].invoke()
                }

                DotsIndicator(
                    totalDots = pages.size,
                    selectedIndex = pagerState.currentPage,
                    modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 6.dp)
                )
            }
        }
    }


    @OptIn(ExperimentalTime::class)
    @Composable
    fun CurrentlyActiveWorkout(
        landingCurrentlyActiveRoutine: SelectedExerciseList,
        elapsedTime: Instant?,
        pageModifier: Modifier
    ) {
        val tabNavigator = LocalTabNavigator.current

        Box(
            modifier = pageModifier.clickable {
                AppNavigator.openBottomSheetCurrentlyActiveWorkoutScreen(
                    landingCurrentlyActiveRoutine,
                    { activeRoutine ->
                        val selectedRoutineList = getSelectedRoutineListFromDB()

                        selectedRoutineList.find { it.routineName == activeRoutine.routineName }
                            ?.let { it.isCompleted = true }

                        setSelectedRoutineListToDB(selectedRoutineList)

                        currentlyActiveRoutine = null
                        viewModel.setCurrentlyActiveRoutine(null)

                        AppNavigator.dismissBottomSheet()
                        (tabNavigator.current as? HomeTab)?.viewModel?.updateContext()
                    },
                    {
                        currentlyActiveRoutine = null
                        viewModel.setCurrentlyActiveRoutine(null)
                        AppNavigator.dismissBottomSheet()
                    }
                )
            }
        ) {
            Image(
                painter = painterResource(Res.drawable.workout_3),
                contentDescription = "Workout image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .blur(80.dp)
            )

            Row(
                modifier = Modifier.fillMaxSize().padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Rounded.Start,
                    contentDescription = "Click",
                    tint = colors.textPrimary,
                    modifier = Modifier.padding(end = 6.dp).size(30.dp)
                )

                SubtitleText(
                    text = "Active: ${landingCurrentlyActiveRoutine.routineName.orEmpty()}",
                    modifier = Modifier.weight(1f)
                )
                TinyText(ElapsedTime(elapsedTime))
            }
        }

    }

    @Composable
    fun CurrentlyPlaying(playback: SpotifyPlayback, pageModifier: Modifier) {
        Box(
            modifier = pageModifier.clickable {
                openNowPlaying()
            }
        ) {
            Image(
                painter = painterResource(Res.drawable.workout_1),
                contentDescription = "Workout image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .matchParentSize()
                    .blur(80.dp)
            )

            Row(
                modifier = Modifier.fillMaxSize().padding(start = 12.dp, end = 12.dp, top = 12.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = playback.item?.album?.images?.firstOrNull()?.url,
                    contentDescription = "Album image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(44.dp)
                        .clip(RoundedCornerShape(4.dp))
                )

                MarqueeSubtitleText(
                    text = playback.item?.name.orEmpty(),
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = "Spotify Previous",
                    tint = colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp).size(30.dp).clickable {
                        viewModel.postPlayerState(SpotifyPlayerState.PREVIOUS)
                    }
                )

                Icon(
                    imageVector = if (playback.is_playing == true) Icons.Rounded.PauseCircleFilled else Icons.Rounded.PlayCircleFilled,
                    contentDescription = "Spotify Play/Pause",
                    tint = colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp).size(40.dp).clickable {
                        viewModel.postPlayerState(if (playback.is_playing == true) SpotifyPlayerState.PAUSE else SpotifyPlayerState.PLAY)
                    }
                )

                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = "Spotify Next",
                    tint = colors.textPrimary,
                    modifier = Modifier.padding(horizontal = 4.dp).size(30.dp).clickable {
                        viewModel.postPlayerState(SpotifyPlayerState.NEXT)
                    }
                )
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    val tripleOfTabIcons = listOf(
        Triple(ProfileTab, Icons.Rounded.Person, Icons.Outlined.Person),
        Triple(HomeTab, Icons.Rounded.Home, Icons.Outlined.Home),
        Triple(ViewAllWorkoutTabScreen, Icons.Rounded.FitnessCenter, Icons.Outlined.FitnessCenter),
    )

    val selectedIcon = tripleOfTabIcons.find { it.first == tab }?.second
    val unselectedIcon = tripleOfTabIcons.find { it.first == tab }?.third

    NavigationBarItem(
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = colors.textPrimary,
            selectedTextColor = colors.textPrimary,
            unselectedIconColor = colors.textPrimary,
            unselectedTextColor = colors.textPrimary,
            indicatorColor = colors.bottomNavIndicator
        ),
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { TinyText(tab.options.title) },
        icon = {
            val it = if (tabNavigator.current == tab) selectedIcon else unselectedIcon
            Icon(
                imageVector = it ?: Icons.Outlined.Home,
                contentDescription = tab.options.title,
            )
        }
    )
}