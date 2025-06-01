package org.gabrieal.gymtracker.features.startWorkout.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.navigation.AppNavigator

class StartWorkoutViewModel {

    private val _uiState = MutableStateFlow(StartWorkoutUiState())

    val uiState: StateFlow<StartWorkoutUiState> = _uiState.asStateFlow()

    private var callback: ((SelectedExerciseList) -> Unit)? = null

    fun setSelectedExerciseList(selectedExerciseList: SelectedExerciseList) {
        _uiState.update { it.copy(selectedExerciseList = selectedExerciseList) }
    }

    fun markWorkoutAsDone() {
        _uiState.update {
            it.copy(
                selectedExerciseList = it.selectedExerciseList?.copy(
                    isCompleted = true
                )
            )
        }

        _uiState.value.selectedExerciseList?.let {
            callback?.invoke(it)
            AppNavigator.navigateBack()
        }
    }

    fun setCallback(callback: (SelectedExerciseList) -> Unit) {
        this.callback = callback
    }
}
