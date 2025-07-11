package org.gabrieal.gymtracker.features.landing.view

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.rounded.FitnessCenter
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.workout_3
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.currentlyActiveRoutine
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
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
import org.gabrieal.gymtracker.util.widgets.CustomHorizontalDivider
import org.gabrieal.gymtracker.util.widgets.MarqueeTinyItalicText
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
        val uiState by viewModel.uiState.collectAsState()
        val landingCurrentlyActiveRoutine = uiState.currentlyActiveRoutine
        val resetCompletedList = uiState.resetCompletedList

        currentlyActiveRoutine = getCurrentlyActiveRoutineFromDB()
        viewModel.setCurrentlyActiveRoutine(currentlyActiveRoutine?.first)
        viewModel.resetCompletedList()

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
                            landingCurrentlyActiveRoutine?.let {
                                CurrentlyActiveWorkout(it, currentlyActiveRoutine?.second)
                            }

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

                    Column(modifier = Modifier.padding(it).background(colors.background)) {
                        Crossfade(targetState = tabNavigator.current) { tab ->
                            tab.Content()
                        }
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
    fun CurrentlyActiveWorkout(
        landingCurrentlyActiveRoutine: SelectedExerciseList,
        elapsedTime: Instant?
    ) {
        val tabNavigator = LocalTabNavigator.current

        Box(modifier = Modifier.clickable {
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
                })
        }) {
            Image(
                painter = painterResource(Res.drawable.workout_3),
                contentDescription = "Workout image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.matchParentSize().blur(80.dp)
            )
            Column(modifier = Modifier.fillMaxWidth()) {
                CustomHorizontalDivider()
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SubtitleText(
                            "Currently Active: ${landingCurrentlyActiveRoutine.routineName.orEmpty()}",
                            modifier = Modifier.weight(1f)
                        )
                        TinyText(ElapsedTime(elapsedTime))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    MarqueeTinyItalicText(
                        landingCurrentlyActiveRoutine.exercises?.joinToString(
                            ", "
                        ) { it.name.orEmpty() } ?: "")
                }
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