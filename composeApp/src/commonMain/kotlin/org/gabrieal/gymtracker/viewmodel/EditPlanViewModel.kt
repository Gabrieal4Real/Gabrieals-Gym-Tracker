package org.gabrieal.gymtracker.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.SelectedExercise
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.views.allExistingExerciseList
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.repRanges

class EditPlanViewModel {
    // Private mutable state flow
    private val _uiState = MutableStateFlow(EditPlanUiState())
    
    // Public immutable state flow that the UI can observe
    val uiState: StateFlow<EditPlanUiState> = _uiState.asStateFlow()

    // Callback to update exercises in parent screen
    private var callback: ((List<SelectedExercise>) -> Unit)? = null

    fun initializeDefaultExerciseList() {
        val defaultListSize = _uiState.value.exercises.size
        val defaultList = List(defaultListSize) { allExistingExerciseList.random().name }
        _uiState.update { it.copy(defaultExerciseList = defaultList) }
    }

    fun setDay(day: String) {
        _uiState.update { it.copy(day = day) }
    }

    fun setExercises(exercises: List<SelectedExercise>) {
        _uiState.update { it.copy(exercises = exercises) }
    }

    fun setCallback(callback: (List<SelectedExercise>) -> Unit) {
        this.callback = callback
    }

    fun setShowImage(show: Boolean) {
        _uiState.update { it.copy(showImage = show) }
    }

    fun setShowRemoveDialog(show: Boolean) {
        _uiState.update { it.copy(showRemoveDialog = show) }
    }

    fun setCurrentClickedPosition(position: Int) {
        _uiState.update { it.copy(currentClickedPosition = position) }
    }

    fun updateExerciseName(position: Int, name: String) {
        val updatedExercises = _uiState.value.exercises.toMutableList().apply {
            this[position] = this[position].copy(name = name)
        }
        _uiState.update { it.copy(exercises = updatedExercises) }
    }

    fun updateExerciseSets(position: Int, sets: Int) {
        val updatedExercises = _uiState.value.exercises.toMutableList().apply {
            this[position] = this[position].copy(sets = sets)
        }
        _uiState.update { it.copy(exercises = updatedExercises) }
    }

    fun updateExerciseReps(position: Int, reps: Pair<Int, Int>) {
        val updatedExercises = _uiState.value.exercises.toMutableList().apply {
            this[position] = this[position].copy(reps = reps)
        }
        _uiState.update { it.copy(exercises = updatedExercises) }
    }

    fun addExercise() {
        val newExercise = SelectedExercise(
            name = "",
            reps = repRanges.random(),
            sets = 3
        )
        val updatedExercises = _uiState.value.exercises.toMutableList().apply {
            add(newExercise)
        }
        
        // Update default exercise list
        val updatedDefaultList = _uiState.value.defaultExerciseList.toMutableList().apply {
            add(allExistingExerciseList.random().name)
        }
        
        _uiState.update { 
            it.copy(
                exercises = updatedExercises,
                defaultExerciseList = updatedDefaultList
            ) 
        }
    }

    fun removeExercise(position: Int) {
        if (_uiState.value.exercises.size > 1) {
            val updatedExercises = _uiState.value.exercises.toMutableList().apply {
                removeAt(position)
            }
            
            // Update default exercise list
            val updatedDefaultList = _uiState.value.defaultExerciseList.toMutableList().apply {
                removeAt(position)
            }
            
            _uiState.update { 
                it.copy(
                    exercises = updatedExercises,
                    defaultExerciseList = updatedDefaultList,
                    showRemoveDialog = false
                ) 
            }
        } else {
            setShowRemoveDialog(false)
        }
    }

    fun navigateToViewAllWorkouts(position: Int) {
        val callback = { exerciseName: String ->
            updateExerciseName(position, exerciseName)
        }

        AppNavigator.navigateToViewAllWorkouts(callback)
    }

    fun navigateBack() {
        //get non-empty exercise list
        val nonEmptyExercises = _uiState.value.exercises.filter { it.name?.isNotBlank() == true }
        if (nonEmptyExercises.isNotEmpty()) {
            callback?.invoke(nonEmptyExercises)
        }

        // Navigate back
        AppNavigator.navigateBack()
    }
}
