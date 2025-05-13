package org.gabrieal.gymtracker.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import gymtracker.composeapp.generated.resources.Res
import gymtracker.composeapp.generated.resources.start_editing
import kotlinx.coroutines.delay
import org.gabrieal.gymtracker.ui.widgets.AnimatedImageFromLeftVisibility
import org.gabrieal.gymtracker.ui.widgets.BackButtonRow
import org.gabrieal.gymtracker.ui.widgets.DescriptionText
import org.gabrieal.gymtracker.ui.widgets.SubtitleText
import org.gabrieal.gymtracker.ui.widgets.TinyItalicText
import org.gabrieal.gymtracker.ui.widgets.TinyText
import org.gabrieal.gymtracker.util.Colors
import org.gabrieal.gymtracker.util.Resources
import org.gabrieal.gymtracker.util.Workout
import org.gabrieal.gymtracker.util.Workout.Companion.fullDays
import org.gabrieal.gymtracker.util.Workout.Companion.plans

data class SplitEditScreen(val selectedDays: Int) : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        val currentPlan = Workout.selectDays(selectedDays.toFloat())
        val activeDays = currentPlan.count { it }

        var animationVisibility by rememberSaveable { mutableStateOf(false) }

        val scrollState = rememberScrollState()
        var hasScrolled by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(scrollState.value) {
            if (scrollState.value != 0 && !hasScrolled) {
                hasScrolled = true
            }

            if (hasScrolled) {
                animationVisibility = false
            }
        }

        LaunchedEffect(Unit) {
            delay(200)
            animationVisibility = true
        }

        Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            BackButtonRow(Resources.strings.makeAPlan)
            Box {
                Column(modifier = Modifier.fillMaxSize().background(Colors.LighterBackground).padding(16.dp).clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = {
                        animationVisibility = false
                    },
                )) {
                    SubtitleText(
                        Resources.strings.xDayWorkoutxDayRest(selectedDays, 7 - selectedDays),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Column (modifier = Modifier.fillMaxSize().verticalScroll(scrollState)) {
                        repeat(7) {
                            Card (shape = RoundedCornerShape(8.dp),
                                backgroundColor = if(currentPlan[it]) Colors.CardBackground else Colors.Maroon,
                                border = BorderStroke(2.dp, Colors.BorderStroke),
                                modifier = Modifier.padding(bottom = 8.dp).fillMaxSize().clickable(
                                    indication = if(currentPlan[it]) LocalIndication.current else null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    onClick = {
                                        animationVisibility = false
                                        if (currentPlan[it])
                                            navigator.push(DayEditScreen(plans[activeDays - 1][it]))
                                    },
                                )) {
                                Column (modifier = Modifier.padding(16.dp)) {
                                    Row (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                                        Column {
                                            DescriptionText(fullDays[it])
                                            if (currentPlan[it]) {
                                                TinyItalicText(plans[activeDays - 1][it], color = Colors.TextSecondary)
                                                TinyText(Resources.strings.notEditedYet)
                                            }
                                        }
                                        Spacer(modifier = Modifier.weight(1f))
                                        if (!currentPlan[it]) {
                                            TinyItalicText(Resources.strings.restDay)
                                        } else {
                                            Icon(imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight, contentDescription = null, tint = Colors.TextPrimary)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                AnimatedImageFromLeftVisibility(animationVisibility, Res.drawable.start_editing)
            }
        }
    }
}