package org.gabrieal.gymtracker.features.viewAllWorkouts.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.util.enums.MuscleGroup
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.allExistingExerciseList

class ViewAllWorkoutViewModel {
    private val _uiState =
        MutableStateFlow(ViewAllWorkoutUiState(filteredWorkouts = allExistingExerciseList))

    val uiState: StateFlow<ViewAllWorkoutUiState> = _uiState.asStateFlow()

    fun setCallback(callback: (String) -> Unit) {
        _uiState.update { it.copy(callback = callback) }
    }

    fun setSearchFilter(filter: String) {
        _uiState.update { it.copy(searchFilter = filter) }
        updateFilteredWorkouts()
    }

    fun toggleFilter(filter: String) {
        val currentFilters = _uiState.value.selectedFilters
        val updatedFilters = if (filter in currentFilters) {
            currentFilters - filter
        } else {
            currentFilters + filter
        }

        _uiState.update { it.copy(selectedFilters = updatedFilters) }
        updateFilteredWorkouts()
    }

    fun updateFilteredWorkouts() {
        val searchFilter = _uiState.value.searchFilter
        val selectedFilters = _uiState.value.selectedFilters

        val filteredList = allExistingExerciseList.filter { exercise ->
            exercise.name.contains(searchFilter, ignoreCase = true) &&
                    (selectedFilters.isEmpty() || selectedFilters.any {
                        MuscleGroup.relatedMuscles(
                            it
                        ).contains(exercise.targetMuscle)
                    })
        }

        _uiState.update { it.copy(filteredWorkouts = filteredList) }
    }

    fun setSelectedWorkout(workout: String) {
        _uiState.update {
            it.copy(
                selectedWorkout = workout,
                showConfirmAddToRoutineDialog = true
            )
        }
    }

    fun dismissConfirmDialog() {
        _uiState.update { it.copy(showConfirmAddToRoutineDialog = false) }
    }

    fun confirmWorkoutSelection() {
        _uiState.value.callback?.invoke(_uiState.value.selectedWorkout)
        dismissConfirmDialog()
        navigateBack()
    }

    fun openYoutubeSearch(workoutName: String) {
        val searchUrl = "https://www.youtube.com/results?search_query=how+to+do+${workoutName.replace(" ", "+")}"
        _uiState.update { it.copy(youtubeUrlToOpen = searchUrl) }
    }

    fun onUrlOpened() {
        _uiState.update { it.copy(youtubeUrlToOpen = null) }
    }

    fun navigateBack() {
        AppNavigator.navigateBack()
    }

    fun getAllMuscleGroups(): List<String> {
        return MuscleGroup.entries.map { it.displayName }
    }
}
