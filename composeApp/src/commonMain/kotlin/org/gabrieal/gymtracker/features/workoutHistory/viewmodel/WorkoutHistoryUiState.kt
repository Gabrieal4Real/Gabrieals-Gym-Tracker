package org.gabrieal.gymtracker.features.workoutHistory.viewmodel

import org.gabrieal.gymtracker.data.model.WorkoutHistory

data class WorkoutHistoryUiState(
    val workoutHistoryList: List<WorkoutHistory> = emptyList(),
    val groupedAndSortedHistory: Map<String, List<WorkoutHistory>> = emptyMap(),
)