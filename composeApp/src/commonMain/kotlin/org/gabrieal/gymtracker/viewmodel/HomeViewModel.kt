package org.gabrieal.gymtracker.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.SelectedExerciseList
import org.gabrieal.gymtracker.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getSelectedRoutineListFromViewModel

class HomeViewModel {
    // Private mutable state flow
    private val _uiState = MutableStateFlow(HomeUiState())
    
    // Public immutable state flow that the UI can observe
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    // Context to be provided from a Composable
    private var context: Any? = null

    fun updateContext(newContext: Any?) {
        context = newContext
        loadRoutines()
    }

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromViewModel(context)
        _uiState.update { 
            it.copy(
                selectedRoutineList = routines,
                hasRoutines = routines.isNotEmpty()
            )
        }
    }

    fun navigateToCreateSplit() {
        AppNavigator.navigateToCreateSplit()
    }
}
