package org.gabrieal.gymtracker.features.profile.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.CalorieInput
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.app.generateGoalBreakdown
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.Gender
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getProfileFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.getSelectedRoutineListFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.setProfileToSharedPreferences

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
        val routines = getSelectedRoutineListFromSharedPreferences()
        _uiState.update {
            if (routines.isEmpty()) { return }
            it.copy(selectedRoutineList = routines)
        }
    }

    fun setWeightHeightBMIClicked(weightHeightBMIClicked: Int) {
        _uiState.update { it.copy(weightHeightBMIClicked = weightHeightBMIClicked) }
    }

    private fun loadProfile() {
        val profile = getProfileFromSharedPreferences()
        _uiState.update {
            it.copy(profile = profile)
        }
    }

    fun updateProfile(profile: Profile) {
        _uiState.update { it.copy(profile = profile) }
        saveProfile()
    }

    private fun saveProfile() {
        val profile = _uiState.value.profile
        profile?.let { setProfileToSharedPreferences(it) }
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

