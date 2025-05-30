package org.gabrieal.gymtracker.viewmodel.profile

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.model.Profile
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getCurrentContext
import org.gabrieal.gymtracker.util.systemUtil.getProfileFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.getSelectedRoutineListFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.providePreferences

class ProfileViewModel {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private var context: Any? = null

    fun updateContext(newContext: Any?) {
        context = newContext
        loadRoutines()
        loadProfile()
    }

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromSharedPreferences(context)
        _uiState.update {
            if (routines.isEmpty()) {
                return
            }
            it.copy(
                selectedRoutineList = routines
            )
        }
    }

    fun setWeightHeightBMIClicked(weightHeightBMIClicked: Int) {
        _uiState.update { it.copy(weightHeightBMIClicked = weightHeightBMIClicked) }
    }

    private fun loadProfile() {
        val profile = getProfileFromSharedPreferences(context)
        _uiState.update {
            it.copy(profile = profile)
        }
    }

    fun updateProfile(profile: Profile) {
        _uiState.update { it.copy(profile = profile) }
        setProfile(true)
    }

    @Composable
    fun saveProfile() {
        val profile = _uiState.value.profile

        getCurrentContext().let {
            providePreferences(it).putString(
                "profile",
                Json.encodeToString(profile)
            )
            setProfile(false)

        }
    }

    fun setProfile(save: Boolean) {
        _uiState.update { it.copy(saveProfile = save) }
    }

    @Composable
    fun saveRoutineList() {
        val sortedRoutineList = _uiState.value.selectedRoutineList.sortedBy { it.position }

        getCurrentContext().let {
            providePreferences(it).putString(
                "selectedRoutineList",
                Json.encodeToString(sortedRoutineList)
            )
            AppNavigator.navigateToRoot()
            setSaveRoutineList(false)
        }
    }

    fun setSaveRoutineList(save: Boolean) {
        _uiState.update { it.copy(saveRoutineList = save) }
    }
    
    fun updateSelectedRoutineList(selectedRoutine: SelectedExerciseList) {
        uiState.value.selectedRoutineList.forEachIndexed { index, routine ->
            if (routine.position == selectedRoutine.position) {
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

