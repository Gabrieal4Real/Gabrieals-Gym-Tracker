package org.gabrieal.gymtracker.features.home.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExerciseList

data class HomeUiState(
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val hasRoutines: Boolean = false
)
