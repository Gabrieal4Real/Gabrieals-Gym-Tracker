package org.gabrieal.gymtracker.features.profile.viewmodel

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.model.Profile
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.model.CalorieInput
import org.gabrieal.gymtracker.util.app.generateGoalBreakdown
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.Gender
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

    fun navigateToEditSplit(routines: List<SelectedExerciseList>) {
        AppNavigator.navigateToEditSplit(routines)
    }

    fun navigateToProteinCalculator() {
        AppNavigator.navigateToCalculatorScreen("Protein Intake", uiState.value.profile)
    }

    fun navigateToMaintenanceCalculator() {
        val input = CalorieInput(
            gender = uiState.value.profile?.gender ?: Gender.MALE,
            age = uiState.value.profile?.age ?: 27,
            weightKg = uiState.value.profile?.weight ?: 70.0,
            heightCm = uiState.value.profile?.height ?: 175.0,
            activityLevel = uiState.value.profile?.activityLevel ?: ActivityLevel.MODERATELY_ACTIVE
        )

        val breakdown = generateGoalBreakdown(input)

        breakdown.forEach {
            println("${it.label} (${it.weightChangePerWeekKg} kg/week): ${it.calories} kcal/day (${it.percentageOfMaintenance}%)")
        }
    }
}

