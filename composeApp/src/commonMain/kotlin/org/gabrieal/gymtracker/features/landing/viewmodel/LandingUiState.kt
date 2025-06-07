package org.gabrieal.gymtracker.features.landing.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExerciseList

data class LandingUiState(
    val currentlyActiveRoutine: SelectedExerciseList? = null,
    val resetCompletedList: Boolean = false
)