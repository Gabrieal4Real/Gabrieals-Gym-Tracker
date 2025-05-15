package org.gabrieal.gymtracker.viewmodel

import org.gabrieal.gymtracker.data.SelectedExercise
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.repRanges

data class EditPlanUiState(
    val day: String = "",
    val exercises: List<SelectedExercise> = emptyList(),
    val showImage: Boolean = true,
    val showRemoveDialog: Boolean = false,
    val currentClickedPosition: Int = 0,
    val defaultExerciseList: List<String> = emptyList()
)
