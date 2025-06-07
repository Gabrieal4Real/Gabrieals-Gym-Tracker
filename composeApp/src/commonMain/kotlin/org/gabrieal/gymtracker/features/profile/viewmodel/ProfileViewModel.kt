package org.gabrieal.gymtracker.features.profile.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.CalorieInput
import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.util.app.generateGoalBreakdown
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.Gender
import org.gabrieal.gymtracker.util.navigation.AppNavigator
import org.gabrieal.gymtracker.util.systemUtil.getFirebaseInfoFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.getProfileFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.getSelectedRoutineListFromSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.setFirebaseInfoToSharedPreferences
import org.gabrieal.gymtracker.util.systemUtil.setProfileToSharedPreferences

class ProfileViewModel {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()


    fun updateContext() {
        loadRoutines()
        loadProfile()
        loadFirebaseInfo()
    }

    private fun loadRoutines() {
        val routines = getSelectedRoutineListFromSharedPreferences()
        _uiState.update {
            if (routines.isEmpty()) {
                return
            }
            it.copy(selectedRoutineList = routines)
        }
    }

    private fun loadProfile() {
        val profile = getProfileFromSharedPreferences()
        _uiState.update {
            it.copy(profile = profile)
        }
    }

    private fun loadFirebaseInfo() {
        val firebaseInfo = getFirebaseInfoFromSharedPreferences()
        _uiState.update {
            it.copy(firebaseInfo = firebaseInfo)
        }
    }

    fun setWeightHeightBMIClicked(weightHeightBMIClicked: Int) {
        _uiState.update { it.copy(weightHeightBMIClicked = weightHeightBMIClicked) }
    }

    private fun updateProfile(update: (Profile) -> Profile) {
        val currentProfile = _uiState.value.profile ?: Profile()
        val updatedProfile = update(currentProfile)
        _uiState.update { it.copy(profile = updatedProfile) }
        saveProfile()
    }

    fun updateWeight(weight: Double?) = updateProfile { it.copy(weight = weight) }

    fun updateHeight(height: Double?) = updateProfile { it.copy(height = height) }

    fun updateAge(age: Int?) = updateProfile { it.copy(age = age) }

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

    fun navigateToLoginRegister() {
        val callback = { profile: Profile? ->
            _uiState.update { it.copy(profile = profile) }
            saveProfile()
            loadFirebaseInfo()
        }

        AppNavigator.openBottomSheetLoginRegisterScreen(
            profile = uiState.value.profile,
            callback = callback
        )
    }

    fun setLoggingOut(loggingOut: Boolean) {
        _uiState.update { it.copy(loggingOut = loggingOut) }
    }

    fun logout() {
        setFirebaseInfoToSharedPreferences(
            FirebaseInfo(
                uid = null,
                token = null
            )
        )
        loadFirebaseInfo()
        setLoggingOut(false)
    }
}

