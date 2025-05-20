package org.gabrieal.gymtracker.views.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.workout_1
import gymtracker.composeapp.generated.resources.workout_2
import gymtracker.composeapp.generated.resources.workout_3
import gymtracker.composeapp.generated.resources.workout_4
import gymtracker.composeapp.generated.resources.workout_5
import gymtracker.composeapp.generated.resources.workout_7
import gymtracker.composeapp.generated.resources.workout_8
import org.gabrieal.gymtracker.util.systemUtil.Resources
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.util.systemUtil.getTodayDayName
import org.gabrieal.gymtracker.viewmodel.home.HomeViewModel
import org.gabrieal.gymtracker.views.colors
import org.gabrieal.gymtracker.views.widgets.BiggerText
import org.gabrieal.gymtracker.views.widgets.TitleRow
import org.jetbrains.compose.resources.painterResource

object HomeScreen : Screen {
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

    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()

        val selectedRoutineList = uiState.selectedRoutineList
        val todayRoutine = selectedRoutineList.find { it.day.equals(getTodayDayName(), ignoreCase = false) }

        val context = getCurrentContext()

        LaunchedEffect(context) {
            viewModel.updateContext(context)
        }

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleRow(Resources.strings.appName)
            Box(
                modifier = Modifier.fillMaxSize().background(colors.lighterBackground)
            ) {
                if (selectedRoutineList.isEmpty()) {
                    return@Box
                }

                Column (modifier = Modifier.verticalScroll(rememberScrollState())){
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.95f)
                            .clickable {
                                //Go to today's workout
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
                                        colorStops = arrayOf(
                                            0.0f to Color.Transparent,
                                            0.7f to Color.Transparent,
                                            1.0f to colors.black.copy(alpha = 1f)
                                        )
                                    )
                                )
                        )

                        BiggerText(
                            "${todayRoutine?.routineName} Day",
                            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
                        )

                        BiggerText(
                            ">",
                            modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
