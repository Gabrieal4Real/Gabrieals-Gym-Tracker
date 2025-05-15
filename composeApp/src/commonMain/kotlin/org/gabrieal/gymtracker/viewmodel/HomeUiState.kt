package org.gabrieal.gymtracker.viewmodel

import org.gabrieal.gymtracker.data.SelectedExerciseList

data class HomeUiState(
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val hasRoutines: Boolean = false
)
