package org.gabrieal.gymtracker.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.gabrieal.gymtracker.util.navigation.AppNavigator

class CreateSplitViewModel {
    // Private mutable state flow
    private val _uiState = MutableStateFlow(CreateSplitUiState())
    
    // Public immutable state flow that the UI can observe
    val uiState: StateFlow<CreateSplitUiState> = _uiState.asStateFlow()

    fun updateSelectedDay(index: Int, selected: Boolean) {
        val currentSelectedDays = _uiState.value.selectedDays.toMutableList()
        
        // Limit to 5 active days
        if (selected && currentSelectedDays.count { it } >= 5) {
            // Find the first selected day and deselect it
            val firstSelectedIndex = currentSelectedDays.indexOfFirst { it }
            if (firstSelectedIndex != -1) {
                currentSelectedDays[firstSelectedIndex] = false
            }
        }
        
        // Update the selected day
        currentSelectedDays[index] = selected
        
        // Update the state
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
