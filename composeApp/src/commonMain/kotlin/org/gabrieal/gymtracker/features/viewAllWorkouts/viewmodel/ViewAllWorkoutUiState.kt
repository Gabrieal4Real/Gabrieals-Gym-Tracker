package org.gabrieal.gymtracker.features.viewAllWorkouts.viewmodel

import org.gabrieal.gymtracker.model.Exercise

data class ViewAllWorkoutUiState(
    val searchFilter: String = "",
    val selectedFilters: Set<String> = emptySet(),
    val selectedWorkout: String = "",
    val showConfirmAddToRoutineDialog: Boolean = false,
    val youtubeUrlToOpen: String? = null,
    val filteredWorkouts: List<Exercise> = emptyList(),
    val callback: ((String) -> Unit)? = null
)
