package org.gabrieal.gymtracker.features.home.viewmodel

import org.gabrieal.gymtracker.data.model.SelectedExerciseList
import org.gabrieal.gymtracker.data.model.SpotifyTracks

data class HomeUiState(
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val hasRoutines: Boolean = false,
    val error: String? = null,
    val spotifyTracks: SpotifyTracks? = null,
)
