package org.gabrieal.gymtracker.features.home.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getSelectedRoutineListFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.setSelectedRoutineListToSharedPreferences

class HomeViewModel {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var context: Any? = null

    fun updateContext(newContext: Any?) {
        context = newContext
        loadRoutines()
    }

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromSharedPreferences()
        _uiState.update {
            if (routines.isEmpty()) {
                navigateToCreateSplit()
            }

            it.copy(
                selectedRoutineList = routines,
                hasRoutines = routines.isNotEmpty()
            )
        }
    }

    private fun navigateToCreateSplit() {
        AppNavigator.navigateToCreateSplit()
    }

    fun navigateToStartWorkout(
        selectedExerciseList: SelectedExerciseList,
        callback: (SelectedExerciseList) -> Unit
    ) {
        AppNavigator.navigateToStartWorkout(selectedExerciseList, callback)
    }

    private fun saveRoutineList() {
        val sortedRoutineList = _uiState.value.selectedRoutineList
        setSelectedRoutineListToSharedPreferences(sortedRoutineList)
        AppNavigator.navigateToRoot()
    }

    fun updateSelectedRoutine(selectedRoutine: SelectedExerciseList) {
        uiState.value.selectedRoutineList.forEachIndexed { index, routine ->
            if (routine.day == selectedRoutine.day) {
                _uiState.update {
                    it.copy(
                        selectedRoutineList = it.selectedRoutineList.toMutableList().apply {
                            this[index] = selectedRoutine
                        }
                    )
                }
                saveRoutineList()
            }
        }
    }
}
