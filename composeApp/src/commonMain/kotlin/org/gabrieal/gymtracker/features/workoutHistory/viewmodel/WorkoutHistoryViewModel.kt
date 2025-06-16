package org.gabrieal.gymtracker.features.workoutHistory.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.WorkoutHistory
import org.gabrieal.gymtracker.util.app.planTitles
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal

class WorkoutHistoryViewModel {
    private val _uiState = MutableStateFlow(WorkoutHistoryUiState())
    val uiState: StateFlow<WorkoutHistoryUiState> = _uiState.asStateFlow()

    fun setWorkoutHistoryList(workoutHistoryList: List<WorkoutHistory>) {
        _uiState.update { it.copy(workoutHistoryList = workoutHistoryList) }

        val groupedHistory: Map<String, List<WorkoutHistory>> =
            workoutHistoryList
                .filter { it.routineName in planTitles && it.finishedDate.isNotBlank() }
                .groupBy { it.routineName ?: "" }
                .mapValues { (_, list) ->
                    list.sortedByDescending { it.finishedDate }
                }

        val sortedWorkoutHistoryList: LinkedHashMap<String, List<WorkoutHistory>> = linkedMapOf()

        planTitles.forEach { title ->
            groupedHistory[title]?.let { sortedWorkoutHistoryList[title] = it }
        }

        _uiState.update { it.copy(groupedAndSortedHistory = sortedWorkoutHistoryList) }
    }
}
