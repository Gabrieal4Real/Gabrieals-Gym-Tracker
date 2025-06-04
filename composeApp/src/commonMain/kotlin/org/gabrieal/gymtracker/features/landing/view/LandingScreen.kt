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
import androidx.compose.material.ExperimentalMaterialApi
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
import org.gabrieal.gymtracker.features.home.view.HomeTab
import org.gabrieal.gymtracker.features.landing.viewmodel.LandingViewModel
import org.gabrieal.gymtracker.features.profile.view.ProfileTab
import org.gabrieal.gymtracker.features.viewAllWorkouts.view.ViewAllWorkoutTabScreen
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.startTime
import org.gabrieal.gymtracker.util.app.ElapsedTimeDisplay
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.widgets.CustomHorizontalDivider
import org.gabrieal.gymtracker.util.widgets.MarqueeTinyItalicText
import org.gabrieal.gymtracker.util.widgets.SubtitleText
import org.gabrieal.gymtracker.util.widgets.TinyText
import org.jetbrains.compose.resources.painterResource
import kotlin.time.ExperimentalTime

object LandingScreen : Screen {
    private val viewModel = LandingViewModel()

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val landingCurrentlyActiveRoutine = uiState.currentlyActiveRoutine

        LaunchedEffect(Unit) {
            viewModel.setCurrentlyActiveRoutine(currentlyActiveRoutine)
        }

        BottomSheetNavigator(
            sheetBackgroundColor = Color.Transparent,
            sheetContentColor = Color.Transparent
        ) { bottomSheetNavigator ->
            LaunchedEffect(bottomSheetNavigator) {
                AppNavigator.setBottomSheetNavigator(bottomSheetNavigator)
            }
            TabNavigator(HomeTab) { tabNavigator ->
                Scaffold(
                    bottomBar = {
                        Column {
                            landingCurrentlyActiveRoutine?.let {
                                CurrentlyActiveWorkout(it)
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
    }

    @OptIn(ExperimentalTime::class)
    @Composable
    fun CurrentlyActiveWorkout(
        landingCurrentlyActiveRoutine: SelectedExerciseList
    ) {
        Box(modifier = Modifier.clickable {
            AppNavigator.openBottomSheetCurrentlyActiveWorkoutScreen(landingCurrentlyActiveRoutine)
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
                        TinyText(ElapsedTimeDisplay(startTime))
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    MarqueeTinyItalicText(
                        landingCurrentlyActiveRoutine?.exercises?.joinToString(
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
            tab.options.icon?.let {
                Icon(
                    painter = it,
                    contentDescription = tab.options.title
                )
            }
        }
    )
}