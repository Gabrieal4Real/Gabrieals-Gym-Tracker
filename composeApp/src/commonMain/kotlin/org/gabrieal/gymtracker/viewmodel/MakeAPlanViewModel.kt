package org.gabrieal.gymtracker.viewmodel

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.data.SelectedExercise
import org.gabrieal.gymtracker.data.SelectedExerciseList
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.getCurrentPlan
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.util.systemUtil.providePreferences

class MakeAPlanViewModel {
    // Private mutable state flow
    private val _uiState = MutableStateFlow(MakeAPlanUiState())
    
    // Public immutable state flow that the UI can observe
    val uiState: StateFlow<MakeAPlanUiState> = _uiState.asStateFlow()

    fun setSelectedDays(selectedDays: List<Boolean>) {
        _uiState.update { it.copy(selectedDays = selectedDays) }
    }

    fun setShowImage(show: Boolean) {
        _uiState.update { it.copy(showImage = show) }
    }

    fun setShowWarningBack(show: Boolean) {
        if (_uiState.value.selectedRoutineList.isEmpty())  {
            navigateBack()
            return
        }

        _uiState.update { it.copy(showWarningBack = show) }
    }

    fun setSaveRoutineList(save: Boolean) {
        _uiState.update { it.copy(saveRoutineList = save) }
    }

    private fun updateSelectedRoutineList(routineList: List<SelectedExerciseList>) {
        _uiState.update { it.copy(selectedRoutineList = routineList) }
    }

    fun navigateBack() {
        updateSelectedRoutineList(listOf())
        AppNavigator.navigateBack()
    }

    fun navigateToEditPlan(index: Int, day: String) {
        val selectedDays = _uiState.value.selectedDays
        val currentPlan = getCurrentPlan(selectedDays)
        val exercises = _uiState.value.selectedRoutineList.find { it.day == day }?.exercises
        
        // Create a callback function to update the exercises
        val callback = { updatedExercises: List<SelectedExercise> ->
            val currentRoutineList = _uiState.value.selectedRoutineList.toMutableList()
            val updatedRoutineList = currentRoutineList
                .filterNot { it.day == day }
                .toMutableList()
                .apply {
                    add(SelectedExerciseList(position = index, day = day, exercises = updatedExercises))
                }
            updateSelectedRoutineList(updatedRoutineList)
        }
        
        // Navigate to edit plan screen
        AppNavigator.navigateToEditPlan(
            day = currentPlan[index],
            exercises = exercises,
            callback = callback
        )
    }

    @Composable
    fun saveRoutineList() {
        val sortedRoutineList = _uiState.value.selectedRoutineList.sortedBy { it.position }
        
        getCurrentContext().let {
            providePreferences(it).putString(
                "selectedRoutineList", 
                Json.encodeToString(sortedRoutineList)
            )
            AppNavigator.navigateToRoot()
            setSaveRoutineList(false)
        }
    }

    fun areAllActiveDaysEdited(): Boolean {
        val activeDaysCount = _uiState.value.selectedDays.count { it }
        val routineListSize = _uiState.value.selectedRoutineList.size
        return activeDaysCount == routineListSize
    }
}
