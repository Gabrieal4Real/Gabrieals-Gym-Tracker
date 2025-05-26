package org.gabrieal.gymtracker.viewmodel.startWorkout

import org.gabrieal.gymtracker.model.SelectedExerciseList

data class StartWorkoutUiState(
    val selectedExerciseList: SelectedExerciseList? = null,
    val isExpanded: List<Boolean>? = null
)