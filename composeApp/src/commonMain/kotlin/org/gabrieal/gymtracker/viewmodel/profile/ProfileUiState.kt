package org.gabrieal.gymtracker.viewmodel.profile

import org.gabrieal.gymtracker.model.Profile
import org.gabrieal.gymtracker.model.SelectedExerciseList

data class ProfileUiState(
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val profile: Profile? = null,
    val saveProfile: Boolean = false,
    val saveRoutineList: Boolean = false,
    val weightHeightBMIClicked: Int = -1
)
