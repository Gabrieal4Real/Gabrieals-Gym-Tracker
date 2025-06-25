package org.gabrieal.gymtracker.features.calculator.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.util.enums.ActivityLevel
import org.gabrieal.gymtracker.util.enums.FitnessGoal

class CalculatorViewModel {
    private val _uiState = MutableStateFlow(CalculatorUiState())

    val uiState: StateFlow<CalculatorUiState> = _uiState.asStateFlow()

    fun setProfile(profile: Profile) = _uiState.update { it.copy(profile = profile) }

    fun setWeight(weight: Int?) = _uiState.update { it.copy(weight = weight) }

    fun setGoal(goal: FitnessGoal?) = _uiState.update { it.copy(goal = goal) }

    fun setActivityLevel(activityLevel: ActivityLevel?) =
        _uiState.update { it.copy(activityLevel = activityLevel) }
}