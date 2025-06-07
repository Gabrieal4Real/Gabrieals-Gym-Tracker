package org.gabrieal.gymtracker.features.makeAPlan.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.SelectedExercise
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.app.getCurrentPlan
import org.gabrieal.gymtracker.util.app.longFormDays
import org.gabrieal.gymtracker.util.app.planTitles
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.formatInstantToDate
import org.gabrieal.gymtracker.util.systemUtil.getMondayOrSameInstant
import org.gabrieal.gymtracker.util.systemUtil.setSelectedRoutineListToSharedPreferences
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class MakeAPlanViewModel {
    private val _uiState = MutableStateFlow(MakeAPlanUiState())

    val uiState: StateFlow<MakeAPlanUiState> = _uiState.asStateFlow()

    fun setSelectedDays(selectedDays: List<Boolean>) {
        _uiState.update { it.copy(selectedDays = selectedDays) }
    }

    fun setShowImage(show: Boolean) {
        _uiState.update { it.copy(showImage = show) }
    }

    fun setShowWarningBack(show: Boolean) {
        if (_uiState.value.selectedRoutineList.isEmpty()) {
            navigateBack()
            return
        }

        _uiState.update { it.copy(showWarningBack = show) }
    }

    fun setOverrideWarning(show: Boolean) {
        _uiState.update { it.copy(showOverrideWarning = show) }
    }

    fun updateSelectedRoutineList(routineList: List<SelectedExerciseList>) {
        _uiState.update { it.copy(selectedRoutineList = routineList) }
    }

    fun navigateBack() {
        updateSelectedRoutineList(listOf())
        AppNavigator.navigateBack()
    }

    @OptIn(ExperimentalTime::class)
    fun navigateToEditPlan(index: Int, day: String) {
        val selectedDays = _uiState.value.selectedDays
        val currentPlan = getCurrentPlan(selectedDays)
        val exercises = _uiState.value.selectedRoutineList.find { it.day == day }?.exercises

        val callback = { updatedExercises: List<SelectedExercise> ->
            val currentRoutineList = _uiState.value.selectedRoutineList.toMutableList()
            val updatedRoutineList = currentRoutineList
                .filterNot { it.day == day }
                .toMutableList()
                .apply {
                    val planTitle = planTitles.find { currentPlan[index].contains(it) }

                    add(
                        SelectedExerciseList(
                            position = longFormDays.indexOf(day),
                            day = day,
                            exercises = updatedExercises,
                            routineName = planTitle,
                            isCompleted = false,
                            startingDate = formatInstantToDate(getMondayOrSameInstant(Clock.System.now()), "dd-MM-yyyy HH:mm:ss")
                        )
                    )
                }
            updateSelectedRoutineList(updatedRoutineList)
        }

        AppNavigator.navigateToEditPlan(
            day = currentPlan[index],
            exercises = exercises,
            callback = callback,
            isEditMode = _uiState.value.isEditMode
        )
    }

    fun saveRoutineList() {
        val sortedRoutineList = _uiState.value.selectedRoutineList
        setSelectedRoutineListToSharedPreferences(sortedRoutineList)
        AppNavigator.navigateToRoot()
    }

    fun areAllActiveDaysEdited(): Boolean {
        val activeDaysCount = _uiState.value.selectedDays.count { it }
        val routineListSize = _uiState.value.selectedRoutineList.size
        return activeDaysCount == routineListSize
    }

    fun setEditMode(isEditMode: Boolean) {
        _uiState.update { it.copy(isEditMode = isEditMode) }
    }
}
