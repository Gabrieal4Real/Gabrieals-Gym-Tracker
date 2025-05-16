package org.gabrieal.gymtracker.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getSelectedRoutineListFromViewModel

class HomeViewModel {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

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
