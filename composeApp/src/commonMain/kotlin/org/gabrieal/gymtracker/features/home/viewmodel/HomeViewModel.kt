package org.gabrieal.gymtracker.features.home.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.sqldelight.getSelectedRoutineListFromDB
import org.gabrieal.gymtracker.data.sqldelight.setFirebaseInfoToDB
import org.gabrieal.gymtracker.data.sqldelight.setSelectedRoutineListToDB
import org.gabrieal.gymtracker.features.home.repository.HomeRepo
import org.gabrieal.gymtracker.features.loginRegister.repository.LoginRegisterRepo
import org.gabrieal.gymtracker.util.navigation.AppNavigator

class HomeViewModel(private val homeRepo: HomeRepo) {
    private val viewModelScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun updateContext() = loadRoutines()

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromDB()
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

    private fun navigateToCreateSplit() = AppNavigator.navigateToCreateSplit()

    fun navigateToStartWorkout(
        selectedExerciseList: SelectedExerciseList,
        callback: (SelectedExerciseList) -> Unit
    ) {
        AppNavigator.navigateToStartWorkout(selectedExerciseList, callback)
    }

    private fun saveRoutineList() {
        val sortedRoutineList = _uiState.value.selectedRoutineList
        setSelectedRoutineListToDB(sortedRoutineList)
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

    fun requestSpotifyToken(trackId: List<String>) {
        viewModelScope.launch {
            AppNavigator.showLoading()
            try {
                val spotifyToken = homeRepo.requestSpotifyToken()
                getTrackInfo(trackId, spotifyToken?.access_token.orEmpty())
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                AppNavigator.hideLoading()
            }
        }
    }

    private fun getTrackInfo(trackId: List<String>, token: String) {
        viewModelScope.launch {
            AppNavigator.showLoading()
            try {
                val track = homeRepo.getTrackInfo(trackId, token)
                println(track)
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message) }
            } finally {
                AppNavigator.hideLoading()
            }
        }
    }
}
