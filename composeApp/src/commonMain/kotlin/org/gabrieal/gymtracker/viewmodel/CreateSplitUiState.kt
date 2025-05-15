package org.gabrieal.gymtracker.viewmodel

data class CreateSplitUiState(
    val selectedDays: List<Boolean> = listOf(true, false, true, false, true, false, false),
    val showImage: Boolean = true
)
