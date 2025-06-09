package org.gabrieal.gymtracker.features.startWorkout.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExerciseList

data class StartWorkoutUiState(
    val selectedExerciseList: SelectedExerciseList? = null,
    val currentActiveExercise: SelectedExerciseList? = null,
    val showWarningReplace: Boolean = false,

    val completedVolume: Double = 0.0,

    val expandedExercises: List<Boolean> = mutableListOf(),
    val exerciseWeights: List<String> = mutableListOf(),
    val exerciseReps: List<List<String>> = mutableListOf(),
    val exerciseSets: List<List<Boolean>> = mutableListOf(),
)