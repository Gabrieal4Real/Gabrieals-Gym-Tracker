package org.gabrieal.gymtracker.features.startWorkout.view

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import org.gabrieal.gymtracker.colors
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.features.startWorkout.viewmodel.StartWorkoutViewModel
import org.gabrieal.gymtracker.startTime
import org.gabrieal.gymtracker.util.app.ElapsedTime
import org.gabrieal.gymtracker.util.app.formatRestTime
import org.gabrieal.gymtracker.util.app.getCurrentTimerInSeconds
import org.gabrieal.gymtracker.util.widgets.ButtonType
import org.gabrieal.gymtracker.util.widgets.ConfirmButton
import org.gabrieal.gymtracker.util.widgets.CustomCard
import org.gabrieal.gymtracker.util.widgets.DashedDivider
import org.gabrieal.gymtracker.util.widgets.DescriptionText
import org.gabrieal.gymtracker.util.widgets.LinkText
import org.gabrieal.gymtracker.util.widgets.TinyButton
import org.gabrieal.gymtracker.util.widgets.TinyText
import kotlin.time.ExperimentalTime

object CurrentlyActiveWorkoutBottomSheet : Screen {
    private val viewModel = StartWorkoutViewModel()

    fun setSelectedExerciseList(selectedExerciseList: SelectedExerciseList) {
        viewModel.setSelectedExerciseList(selectedExerciseList)
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    override fun Content() {
        val uiState by viewModel.uiState.collectAsState()
        val selectedExerciseList = uiState.selectedExerciseList
        val completedVolume = uiState.completedVolume
        val completedSets = uiState.completedSets
        val totalSets = selectedExerciseList?.exercises?.sumOf { it.sets ?: 0 }

        Box(
            modifier = Modifier.fillMaxSize()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(colors.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ActiveWorkoutHeader(completedSets, totalSets)
                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    stickyHeader {
                        DurationVolumeSetCard(completedVolume, completedSets)
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    selectedExerciseList?.exercises?.let {
                        items(it.size) { index ->
                            ExerciseItem(it[index])
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ActiveWorkoutHeader(completedSets: Int?, totalSets: Int?) {

    }


    @OptIn(ExperimentalTime::class)
    @Composable
    fun DurationVolumeSetCard(completedVolume: Int, completedSets: Int) {
        CustomCard(
            enabled = false,
            content = {
                val titleMessage = listOf(
                    Pair("Duration", ElapsedTime(startTime)),
                    Pair("Volume", "$completedVolume kg"),
                    Pair("Sets", completedSets)
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    titleMessage.forEachIndexed { index, _ ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            DescriptionText(titleMessage[index].first)
                            TinyText(titleMessage[index].second.toString())
                        }

                        if (index != titleMessage.lastIndex) {
                            Spacer(modifier = Modifier.width(8.dp))
                            DashedDivider()
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                    }
                }
            }
        )
    }

    @Composable
    fun ExerciseItem(exercise: SelectedExercise) {
        var expanded by remember { mutableStateOf(false) }
        val setCount = exercise.sets ?: 0
        val isCompletedList = remember { MutableList(setCount) { false }.toMutableStateList() }

        CustomCard(
            backgroundEnabled = false,
            enabled = true,
            onClick = { expanded = !expanded },
            content = {
                Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    // Header Section
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.weight(1f)) {
                            DescriptionText(exercise.name.orEmpty())

                            if (!expanded) {
                                TinyText(
                                    "${isCompletedList.count { it }} / $setCount done",
                                    color = colors.textSecondary
                                )
                                return@Column
                            }

                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Rounded.Timer,
                                    contentDescription = "Timer",
                                    tint = colors.slightlyDarkerLinkBlue
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                LinkText(
                                    "Rest Timer: ${
                                        formatRestTime(getCurrentTimerInSeconds(exercise.reps))
                                    }"
                                )
                            }
                        }

                        Icon(
                            imageVector = if (expanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                            contentDescription = "Expandable",
                            tint = colors.white
                        )
                    }

                    // Expanded Set List
                    if (expanded) {
                        Spacer(modifier = Modifier.height(8.dp))

                        // Headers
                        Row(modifier = Modifier.fillMaxWidth()) {
                            listOf("Set", "Previous", "KG", "Reps", "").forEach {
                                TinyText(
                                    it.uppercase(),
                                    textAlign = TextAlign.Center,
                                    modifier = if (it.isNotEmpty()) Modifier.weight(1f) else Modifier.weight(1f).width(28.dp),
                                    color = colors.textSecondary
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Set Rows
                        repeat(setCount) { index ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                DescriptionText(
                                    "${index + 1}",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                                TinyText(
                                    "-",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                                DescriptionText(
                                    "10",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                                DescriptionText(
                                    "12",
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                                Icon(
                                    imageVector = if (isCompletedList[index]) Icons.Rounded.CheckCircle else Icons.Rounded.CheckCircleOutline,
                                    contentDescription = "Completed",
                                    tint = if (isCompletedList[index]) colors.checkMarkGreen else colors.placeholderColor,
                                    modifier = Modifier.size(28.dp).weight(1f).clickable {
                                        isCompletedList[index] = !isCompletedList[index]
                                    }
                                )
                            }
                        }

                        TinyButton(
                            modifier = Modifier.fillMaxWidth(0.8f).padding(top = 8.dp).align(Alignment.CenterHorizontally),
                            text = "+ Add Set",
                            onClick = {
                                
                            },
                            buttonType = ButtonType.OUTLINE
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}