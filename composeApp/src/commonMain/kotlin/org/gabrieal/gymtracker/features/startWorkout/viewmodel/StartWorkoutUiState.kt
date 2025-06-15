package org.gabrieal.gymtracker.features.startWorkout.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.WorkoutProgress

data class StartWorkoutUiState(
    val selectedExerciseList: SelectedExerciseList? = null,
    val currentActiveExercise: SelectedExerciseList? = null,
    val showWarningReplace: Boolean = false,

    val completedVolume: Double = 0.0,

    val expandedExercises: List<Boolean> = mutableListOf(),
    val workoutProgress: WorkoutProgress = WorkoutProgress(),

    val totalTime: Int = 0,
    val currentTime: Int = 0,
    val isRunning: Boolean = false,
    val showNotification: Boolean = false
)