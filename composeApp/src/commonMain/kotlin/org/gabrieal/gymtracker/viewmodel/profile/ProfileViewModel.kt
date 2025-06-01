package org.gabrieal.gymtracker.viewmodel.profile

import androidx.compose.runtime.Composable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.json.Json
import org.gabrieal.gymtracker.model.Profile
import org.gabrieal.gymtracker.model.SelectedExerciseList
import org.gabrieal.gymtracker.model.ActivityLevel
import org.gabrieal.gymtracker.model.CalorieInput
import org.gabrieal.gymtracker.model.FitnessGoal
import org.gabrieal.gymtracker.model.Gender
import org.gabrieal.gymtracker.model.ProteinInput
import org.gabrieal.gymtracker.util.appUtil.calculateProteinGrams
import org.gabrieal.gymtracker.util.appUtil.generateGoalBreakdown
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
        val sortedRoutineList = _uiState.value.selectedRoutineList

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
            if (routine.day == selectedRoutine.day) {
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

    fun navigateToEditSplit(routines: List<SelectedExerciseList>) {
        AppNavigator.navigateToEditSplit(routines)
    }

    fun navigateToProteinCalculator() {
        val protein = calculateProteinGrams(
            ProteinInput(weightKg = uiState.value.profile?.weight ?: 0.0, goal = uiState.value.profile?.goal ?: FitnessGoal.MAINTENANCE, activityLevel = uiState.value.profile?.activityLevel ?: ActivityLevel.MODERATELY_ACTIVE)
        )

        println("Recommended protein: $protein g/day")
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

