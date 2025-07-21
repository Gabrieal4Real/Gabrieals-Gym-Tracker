package org.gabrieal.gymtracker.features.profile.viewmodel

import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExerciseList

data class ProfileUiState(
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val profile: Profile? = null,
    val weightHeightBMIClicked: Int = -1,
    val loggingOut: Boolean = false,

    val spotifyUrl: String? = null,
    val error: String? = null,
    val accessToken: String? = null
)
