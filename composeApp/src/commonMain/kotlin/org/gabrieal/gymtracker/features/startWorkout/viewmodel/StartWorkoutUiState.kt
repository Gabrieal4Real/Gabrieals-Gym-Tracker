package org.gabrieal.gymtracker.features.startWorkout.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExerciseList

data class StartWorkoutUiState(
    val selectedExerciseList: SelectedExerciseList? = null,
    val currentActiveExercise: SelectedExerciseList? = null,
    val showWarningReplace: Boolean = false,

    val completedVolume: Int = 0,
    val completedSets: Int = 0,
)