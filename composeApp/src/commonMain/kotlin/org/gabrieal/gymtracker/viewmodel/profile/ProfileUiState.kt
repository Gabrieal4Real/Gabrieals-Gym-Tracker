package org.gabrieal.gymtracker.viewmodel.profile

import org.gabrieal.gymtracker.model.SelectedExerciseList

data class ProfileUiState(
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
)
