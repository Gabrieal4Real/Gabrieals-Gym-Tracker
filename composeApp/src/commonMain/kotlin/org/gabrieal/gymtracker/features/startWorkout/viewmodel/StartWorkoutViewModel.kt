package org.gabrieal.gymtracker.features.startWorkout.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.currentlyActiveRoutine
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.startTime
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

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

    @OptIn(ExperimentalTime::class)
    fun startWorkout(currentActiveExercise: SelectedExerciseList?) {
        currentlyActiveRoutine = currentActiveExercise
        _uiState.update { it.copy(currentActiveExercise = currentActiveExercise) }

        currentActiveExercise?.let {
            startTime = Clock.System.now()
        }
    }

    fun setShowWarningReplace(show: Boolean) {
        _uiState.update { it.copy(showWarningReplace = show) }
    }
}
