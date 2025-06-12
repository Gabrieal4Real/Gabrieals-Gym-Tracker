package org.gabrieal.gymtracker.features.landing.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.app.resetAllCompletedStatus
import org.gabrieal.gymtracker.data.sqldelight.getSelectedRoutineListFromDB

class LandingViewModel {
    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()

    fun setCurrentlyActiveRoutine(currentlyActiveRoutine: SelectedExerciseList?) {
        _uiState.update {
            it.copy(currentlyActiveRoutine = currentlyActiveRoutine)
        }
    }

    fun resetCompletedList() {
        setCompletedRoutineList(resetAllCompletedStatus(getSelectedRoutineListFromDB()))
    }

    fun setCompletedRoutineList(resetCompletedList: Boolean) {
        _uiState.update { it.copy(resetCompletedList = resetCompletedList) }
    }
}
