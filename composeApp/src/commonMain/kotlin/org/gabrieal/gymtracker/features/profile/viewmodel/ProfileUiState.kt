package org.gabrieal.gymtracker.features.profile.viewmodel

import org.gabrieal.gymtracker.data.model.FirebaseInfo
import org.gabrieal.gymtracker.data.model.Profile
import org.gabrieal.gymtracker.data.model.SelectedExerciseList

data class ProfileUiState(
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val profile: Profile? = null,
    val weightHeightBMIClicked: Int = -1,
    val firebaseInfo: FirebaseInfo? = null
)
