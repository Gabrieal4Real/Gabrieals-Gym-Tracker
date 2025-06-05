package org.gabrieal.gymtracker.features.makeAPlan.viewmodel

import org.gabrieal.gymtracker.model.SelectedExerciseList

data class MakeAPlanUiState(
    val selectedDays: List<Boolean> = listOf(true, false, true, false, true, false, false),
    val selectedRoutineList: List<SelectedExerciseList> = emptyList(),
    val showImage: Boolean = true,
    val showWarningBack: Boolean = false,
    val showOverrideWarning: Boolean = false,
    val isEditMode: Boolean = false
)