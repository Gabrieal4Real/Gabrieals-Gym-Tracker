package org.gabrieal.gymtracker.features.home.viewmodel

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.util.systemUtil.getSelectedRoutineListFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.providePreferences

class HomeViewModel {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var context: Any? = null

    fun updateContext(newContext: Any?) {
        context = newContext
        loadRoutines()
    }

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromSharedPreferences(context)
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

    @Composable
    fun saveRoutineList() {
        val sortedRoutineList = _uiState.value.selectedRoutineList

        getCurrentContext().let {
            providePreferences(it).putString(
                "selectedRoutineList",
                Json.encodeToString(sortedRoutineList)
            )
            AppNavigator.navigateToRoot()
            setSaveRoutineList(false)
        }
    }

    private fun setSaveRoutineList(save: Boolean) {
        _uiState.update { it.copy(saveRoutineList = save) }
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
                setSaveRoutineList(true)
            }
        }
    }
}
