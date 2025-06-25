package org.gabrieal.gymtracker.features.workoutHistory.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.WorkoutHistory
import org.gabrieal.gymtracker.data.model.WorkoutProgress

data class WorkoutHistoryUiState(
    val workoutHistoryList: List<WorkoutHistory> = emptyList(),
    val groupedAndSortedHistory: Map<String, List<WorkoutHistory>> = emptyMap(),
    val expandedExercise: Triple<SelectedExercise?, WorkoutProgress?, Int?>? = null
)