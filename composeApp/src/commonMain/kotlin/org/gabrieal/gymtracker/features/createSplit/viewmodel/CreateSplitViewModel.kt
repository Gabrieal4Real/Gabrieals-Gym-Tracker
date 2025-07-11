package org.gabrieal.gymtracker.features.createSplit.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.app.longFormDays
import org.gabrieal.gymtracker.util.navigation.AppNavigator

class CreateSplitViewModel {
    private val _uiState = MutableStateFlow(CreateSplitUiState())

    val uiState: StateFlow<CreateSplitUiState> = _uiState.asStateFlow()

    fun updateSelectedDay(index: Int, selected: Boolean) {
        val currentSelectedDays = _uiState.value.selectedDays.toMutableList()

        if (selected && currentSelectedDays.count { it } >= 5) {
            val firstSelectedIndex = currentSelectedDays.indexOfFirst { it }
            if (firstSelectedIndex != -1) {
                currentSelectedDays[firstSelectedIndex] = false
            }
        }

        currentSelectedDays[index] = selected

        _uiState.update { it.copy(selectedDays = currentSelectedDays) }
    }

    fun setShowImage(show: Boolean) = _uiState.update { it.copy(showImage = show) }

    fun isAnyDaySelected() = _uiState.value.selectedDays.any { it }

    fun navigateToMakeAPlan() {
        if (!isAnyDaySelected()) return

        if (_uiState.value.isEditMode) {
            val routines = _uiState.value.selectedRoutineList.toMutableList()

            _uiState.value.selectedDays.forEachIndexed { index, selected ->
                if (!selected) {
                    routines.removeAll { it.day == longFormDays.elementAt(index) }
                }
            }

            AppNavigator.navigateToMakeAPlan(
                _uiState.value.selectedDays,
                routines,
                _uiState.value.isEditMode
            )
        }

        AppNavigator.navigateToMakeAPlan(
            _uiState.value.selectedDays,
            listOf(),
            _uiState.value.isEditMode
        )
    }

    fun isEditMode(routines: List<SelectedExerciseList>) {
        val currentSelectedDays = mutableListOf(false, false, false, false, false, false, false)

        routines.forEach { routine ->
            longFormDays.indexOf(routine.day).let { index ->
                currentSelectedDays[index] = true
            }
        }

        _uiState.update {
            it.copy(
                isEditMode = routines.isNotEmpty(),
                selectedRoutineList = routines,
                selectedDays = currentSelectedDays,
            )
        }
    }
}
