package org.gabrieal.gymtracker.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.model.SelectedExercise
import org.gabrieal.gymtracker.util.appUtil.Workout.Companion.repRanges
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.views.allExistingExerciseList

class EditPlanViewModel {
    private val _uiState = MutableStateFlow(EditPlanUiState())
    
    val uiState: StateFlow<EditPlanUiState> = _uiState.asStateFlow()

    private var callback: ((List<SelectedExercise>) -> Unit)? = null

    fun initializePlaceholderList() {
        val defaultListSize = _uiState.value.exercises.size
        val placeHolderList = List(defaultListSize) { allExistingExerciseList.random().name }
        _uiState.update { it.copy(placeHolderList = placeHolderList) }
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
        
        val placeholderList = _uiState.value.placeHolderList.toMutableList().apply {
            add(allExistingExerciseList.random().name)
        }
        
        _uiState.update { 
            it.copy(
                exercises = updatedExercises,
                placeHolderList = placeholderList
            ) 
        }
    }

    fun removeExercise(position: Int) {
        if (_uiState.value.exercises.size > 1) {
            val updatedExercises = _uiState.value.exercises.toMutableList().apply {
                removeAt(position)
            }

            val placeholderList = _uiState.value.placeHolderList.toMutableList().apply {
                removeAt(position)
            }
            
            _uiState.update { 
                it.copy(
                    exercises = updatedExercises,
                    placeHolderList = placeholderList,
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
        val nonEmptyExercises = _uiState.value.exercises.filter { it.name?.isNotBlank() == true }
        if (nonEmptyExercises.isNotEmpty()) {
            callback?.invoke(nonEmptyExercises)
        }

        AppNavigator.navigateBack()
    }
}
