package org.gabrieal.gymtracker.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

        _uiState.value = _uiState.value.copy(selectedDays = currentSelectedDays)
    }

    fun setShowImage(show: Boolean) {
        _uiState.value = _uiState.value.copy(showImage = show)
    }

    fun isAnyDaySelected(): Boolean {
        return _uiState.value.selectedDays.any { it }
    }

    fun navigateToMakeAPlan() {
        if (isAnyDaySelected()) {
            AppNavigator.navigateToMakeAPlan(_uiState.value.selectedDays)
        }
    }
}
