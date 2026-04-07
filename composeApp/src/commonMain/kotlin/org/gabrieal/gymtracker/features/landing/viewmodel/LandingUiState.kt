package org.gabrieal.gymtracker.features.landing.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.SpotifyPlayback

data class LandingUiState(
    val currentlyActiveRoutine: SelectedExerciseList? = null,
    val resetCompletedList: Boolean = false,
    val spotifyPlayback: SpotifyPlayback? = null
)