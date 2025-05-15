package org.gabrieal.gymtracker.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.Routine
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.views.allExistingExerciseList

class ViewAllWorkoutViewModel {
    // Private mutable state flow
    private val _uiState = MutableStateFlow(ViewAllWorkoutUiState(
        filteredWorkouts = allExistingExerciseList
    ))
    
    // Public immutable state flow that the UI can observe
    val uiState: StateFlow<ViewAllWorkoutUiState> = _uiState.asStateFlow()
    
    // Callback to send selected exercise back to previous screen
    private var callback: ((String) -> Unit)? = null

    fun setCallback(callback: (String) -> Unit) {
        this.callback = callback
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

    private fun updateFilteredWorkouts() {
        val searchFilter = _uiState.value.searchFilter
        val selectedFilters = _uiState.value.selectedFilters
        
        val filteredList = allExistingExerciseList.filter { exercise ->
            exercise.name.contains(searchFilter, ignoreCase = true) &&
                    (selectedFilters.isEmpty() || exercise.muscleGroup.containsAll(selectedFilters))
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
        callback?.invoke(_uiState.value.selectedWorkout)
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
        return Routine.MuscleGroup.entries.map { it.displayName }
    }
}
