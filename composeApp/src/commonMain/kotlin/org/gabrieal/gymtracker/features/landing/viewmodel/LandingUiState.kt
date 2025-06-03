package org.gabrieal.gymtracker.features.landing.viewmodel

import org.gabrieal.gymtracker.model.SelectedExerciseList

data class LandingUiState(
    val currentlyActiveRoutine: SelectedExerciseList? = null
)