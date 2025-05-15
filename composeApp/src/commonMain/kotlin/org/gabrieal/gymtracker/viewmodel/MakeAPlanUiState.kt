package org.gabrieal.gymtracker.viewmodel

import org.gabrieal.gymtracker.data.SelectedExerciseList

data class MakeAPlanUiState(
    val selectedDays: List<Boolean> = listOf(true, false, true, false, true, false, false),
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val showImage: Boolean = true,
    val showWarningBack: Boolean = false,
    val saveRoutineList: Boolean = false
)
